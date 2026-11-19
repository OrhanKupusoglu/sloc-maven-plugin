package kupusoglu.orhan.sloc_maven_plugin.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.regex.Matcher;

class CommonTest {

    @Test
    void testTrimPackageName() throws Exception {
        String packageName = "sampleapp.cli.commands";
        assertEquals("sampleapp.cli.", Common.trimPackageName(packageName));
    }

    @Test
    void testCountLines() throws MojoExecutionException, IOException {
        Path baseDir = Paths.get(new File(".").getAbsolutePath().toString(), "src/test/resources").normalize();
        String srcMain = "src";
        String fileExt = "java";
        boolean trimPkgNames = true;
        boolean display = true;
        boolean save = false;

        try ( // capture output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos)) {
            System.setOut(ps);

            Common.countLines(new SystemStreamLog(),
                              baseDir.toString(),
                              srcMain,
                              fileExt,
                              StandardCharsets.UTF_8,
                              trimPkgNames,
                              Common.Border.ASCII,
                              Common.DEFAULT_MAX_FILE_NAME_LEN,
                              display,
                              save);

            System.setOut(System.out);

            String[] output = baos.toString().split("\r\n|\r|\n");

            assertTrue(output.length > 0);

            String[] table = Common.removeArrayElement(output, 0);
            String actual = String.join(System.lineSeparator(), table);

            System.out.println(System.lineSeparator() + "ACTUAL OUTPUT:" + System.lineSeparator());
            System.out.println(actual + System.lineSeparator());

            String expected = String.join(System.lineSeparator(), Common.readTextFile(Paths.get(baseDir.toString(), "sloc.txt"), StandardCharsets.UTF_8));
            System.out.println("EXPECTED OUTPUT:" + System.lineSeparator());
            System.out.println(expected + System.lineSeparator());

            assertEquals(expected, actual);
        }
    }

    @Test
    void getCommonPackagePrefix() {
        assertNull(Common.getCommonPackagePrefix(null));
        assertNull(Common.getCommonPackagePrefix(new String[0]));
        assertEquals("", Common.getCommonPackagePrefix(new String[] {""}));
        assertEquals("kupusoglu.orhan", Common.getCommonPackagePrefix(new String[] {"kupusoglu.orhan.a", "kupusoglu.orhan.b", "kupusoglu.orhan"}));
        assertEquals("a.b", Common.getCommonPackagePrefix(new String[] {"a.b.c", "a.b", "a.b.c.d"}));
        assertEquals("a.b", Common.getCommonPackagePrefix(new String[] {"a.b.c.d", "a.b.c", "a.b"}));
        assertEquals("a.b", Common.getCommonPackagePrefix(new String[] {"a.b.c.d", "a.b.c", null, "a.b"}));
        assertEquals("com", Common.getCommonPackagePrefix(new String[] {"com.foo.a", "com.foobar.b"}));
    }

    @Test
    void getCommonPackagePrefix_prefersPrefixSharedByMostPackages() {
        assertEquals("kupusoglu.orhan.sloc_maven_plugin", Common.getCommonPackagePrefix(
            new String[] {"foo.bar", "kupusoglu.orhan.sloc_maven_plugin.engine", "kupusoglu.orhan.sloc_maven_plugin.goal"}));
        assertEquals("com.google.common", Common.getCommonPackagePrefix(
            new String[] {"", "com.google.common.base", "com.google.common.collect"}));
    }

    @Test
    void getCommonPackagePrefix_withoutPrefixSharedByTwoPackages() {
        assertEquals("", Common.getCommonPackagePrefix(new String[] {"a.b", "c.d"}));
        assertEquals("", Common.getCommonPackagePrefix(new String[] {"", "com.google.common"}));
    }

    @Test
    void getCommonPackagePrefix_prefersCoverageOverDepth() {
        // "a" covers three packages, the deeper "b.c.d" covers only two
        assertEquals("a", Common.getCommonPackagePrefix(
            new String[] {"a.x", "a.y", "a.z", "b.c.d.e", "b.c.d.f"}));
    }

    @Test
    void getCommonPackagePrefix_tieOnCoverageAndLength() {
        // two clusters of equal size and depth, the alphabetically first prefix wins
        assertEquals("a", Common.getCommonPackagePrefix(new String[] {"a.x", "a.y", "b.c", "b.d"}));
    }

    @Test
    void getCommonPackagePrefix_deeperHierarchiesWithDifferentRoots() {
        assertEquals("com.acme", Common.getCommonPackagePrefix(
            new String[] {"com.acme.app.core", "com.acme.app.web", "com.acme.lib.util", "org.other.stuff"}));
    }

    @Test
    void getCommonPackagePrefix_singleSegmentCommonPackage() {
        // the caller backs off from "x" because it would eliminate the package "x" completely
        assertEquals("x", Common.getCommonPackagePrefix(new String[] {"x", "x.y", "o.z"}));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "package a.b;               | a.b",
        "package a.b                | a.b",
        "package a.b // comment     | a.b",
        "package a.b; // comment    | a.b",
        "package /* comment */ a.b; | a.b",
        "package /* comment */a.b   | a.b",
        "package a.v2_api.$gen;     | a.v2_api.$gen",
        "@Deprecated package a.b;   | a.b"
    })
    void packageDeclaration_javaOrGroovy_packageNameFound(String line, String expected) {
        Matcher matcher = Common.PACKAGE_DECLARATION.matcher(line);

        assertTrue(matcher.find());
        assertEquals(expected, matcher.group(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"import a.b;", "subpackage a.b;", "packages a.b;", "package;"})
    void packageDeclaration_noDeclaration_notFound(String line) {
        assertFalse(Common.PACKAGE_DECLARATION.matcher(line).find());
    }

    @ParameterizedTest
    @CsvSource({
        "foo.bar,   foo.bar.baz, bar,   bar.baz",
        "com.foo.a, com.foobar.b, foo.a, foobar.b",
        "a.b.a.c,   a.x,          b.a.c, x"
    })
    void processSLOCData_trimPkgNames_commonPrefixRemoved(String package1, String package2, String expected1, String expected2) {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put(package1 + ":A.java:src", new int[4]);
        locData.put(package2 + ":B.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", true).toString().split("\n");

        assertEquals(expected1, CountSLOCTest.findRow(table, "A.java")[0]);
        assertEquals(expected2, CountSLOCTest.findRow(table, "B.java")[0]);
    }

    @Test
    void processSLOCData_trimPkgNames_unrelatedPackageDisplayedInFull() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("out.pkg:A.java:src", new int[4]);
        locData.put("com.foo.b:B.java:src", new int[4]);
        locData.put("com.foo.c:C.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", true).toString().split("\n");

        assertEquals("out.pkg", CountSLOCTest.findRow(table, "A.java")[0]);
        assertEquals("b", CountSLOCTest.findRow(table, "B.java")[0]);
        assertEquals("c", CountSLOCTest.findRow(table, "C.java")[0]);
    }

    @Test
    void processSLOCData_trimPkgNames_outlierWithCommonPackageInSet() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("o.z:A.java:src", new int[4]);
        locData.put("p.q:B.java:src", new int[4]);
        locData.put("p.q.r:C.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", true).toString().split("\n");

        assertEquals("o.z", CountSLOCTest.findRow(table, "A.java")[0]);
        assertEquals("q", CountSLOCTest.findRow(table, "B.java")[0]);
        assertEquals("q.r", CountSLOCTest.findRow(table, "C.java")[0]);
    }

    @Test
    void processSLOCData_trimPkgNames_tableAlignedWithFullLengthOutlier() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("org.example.a.very.long.outlier.package:A.java:src", new int[4]);
        locData.put("com.foo.b:B.java:src", new int[4]);
        locData.put("com.foo.c:C.java:src", new int[4]);

        String[] lines = Common.processSLOCData(locData, "java", true).toString().split("\n");

        // every table line has the same width, a long outlier package name does not break the layout
        assertEquals(1, Arrays.stream(lines).map(String::length).distinct().count());
    }

    @Test
    void processSLOCData_trimPkgNamesFalse_allPackagesInFull() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("out.pkg:A.java:src", new int[4]);
        locData.put("com.foo.b:B.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", false).toString().split("\n");

        assertEquals("out.pkg", CountSLOCTest.findRow(table, "A.java")[0]);
        assertEquals("com.foo.b", CountSLOCTest.findRow(table, "B.java")[0]);
    }

    @Test
    void processSLOCData_trimPkgNames_headerMarkedShort() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("com.foo.b:A.java:src", new int[4]);
        locData.put("com.foo.c:B.java:src", new int[4]);

        String[] trimmed = Common.processSLOCData(locData, "java", true).toString().split("\n");
        String[] untrimmed = Common.processSLOCData(locData, "java", false).toString().split("\n");

        TreeMap<String, int[]> withoutCommonPrefixData = new TreeMap<>();
        withoutCommonPrefixData.put("a.b:A.java:src", new int[4]);
        withoutCommonPrefixData.put("c.d:B.java:src", new int[4]);

        String[] withoutCommonPrefix = Common.processSLOCData(withoutCommonPrefixData, "java", true).toString().split("\n");

        assertEquals("Short Package Name", trimmed[1].split("\\|")[1].trim());
        assertEquals("Package Name", untrimmed[1].split("\\|")[1].trim());
        assertEquals("Package Name", withoutCommonPrefix[1].split("\\|")[1].trim());
    }

    @ParameterizedTest
    @CsvSource({
        "45, File Name",
        "16, Short File Name"
    })
    void processSLOCData_maxFileNameLen_headerMarkedShortIfAbbreviated(int maxFileNameLen, String expected) {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:AVeryLongClassName.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", false, Common.Border.ASCII, maxFileNameLen).toString().split("\n");

        assertEquals(expected, table[1].split("\\|")[2].trim());
        assertEquals(table[0].length(), table[1].length());
    }

    @Test
    void processSLOCData_subpackage_listedAfterParent() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b.c:A.java:src", new int[4]);
        locData.put("a.b:B.java:src", new int[4]);
        locData.put("a.b:C.java:test", new int[4]);
        locData.put("a.bc:D.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", false).toString().split("\n");
        String[] files = Arrays.stream(table)
            .map(line -> line.split("\\|"))
            .filter(cells -> cells.length > 2 && cells[2].trim().endsWith(".java"))
            .map(cells -> cells[2].trim())
            .toArray(String[]::new);

        assertArrayEquals(new String[] {"B.java", "C.java", "A.java", "D.java"}, files);
    }

    @ParameterizedTest
    @CsvSource({
        "0, file,    0 files",
        "1, file,    1 file",
        "1, package, 1 package",
        "2, package, 2 packages"
    })
    void countNoun_count_pluralUnlessOne(int count, String noun, String expected) {
        assertEquals(expected, Common.countNoun(count, noun));
    }

    @Test
    void processSLOCData_singleFile_footerInSingular() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:A.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", false).toString().split("\n");

        assertEquals("1 package", CountSLOCTest.findRow(table, "1 file")[0]);
    }

    @Test
    void processSLOCData_numberColumns_headersRightAligned() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:A.java:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "java", false).toString().split("\n");

        assertTrue(table[1].endsWith("| Type |   Blank | Javadoc | Comment |    Code |   Total |"), table[1]);
    }

    @Test
    void processSLOCData_largeTotal_numberColumnsWidened() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:A.java:src", new int[] {0, 0, 0, 12345678});

        String[] table = Common.processSLOCData(locData, "java", false).toString().split("\n");

        assertTrue(table[1].endsWith("|    Blank |  Javadoc |  Comment |     Code |    Total |"), table[1]);
        assertTrue(table[3].endsWith("|        0 |        0 |        0 | 12345678 | 12345678 |"), table[3]);
    }

    @Test
    void processSLOCData_longFileExt_typeColumnWidened() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:A.groovy:src", new int[4]);

        String[] table = Common.processSLOCData(locData, "groovy", false).toString().split("\n");

        assertTrue(table[1].contains("| Type   |"), table[1]);
        assertTrue(table[3].contains("| src    |"), table[3]);
        assertTrue(table[5].contains("| groovy |"), table[5]);
    }

    @ParameterizedTest
    @CsvSource({
        "a.b.c,         5, a.b.c",
        "com.foo.bar,   8, ..bar",
        "com.foo.bar,   9, ..foo.bar",
        "a.longsegment, 8, ..egment"
    })
    void abbreviatePackageName_length_leadingSegmentsReplaced(String packageName, int maxLength, String expected) {
        assertEquals(expected, Common.abbreviatePackageName(packageName, maxLength));
    }

    @ParameterizedTest
    @CsvSource({
        "Abc.java,        8, Abc.java",
        "AbcdefXyz.java, 11, Ab..yz.java",
        "AbcdefXyz.java, 12, Abc..yz.java",
        "AbcdefXyz.java, 13, Abc..Xyz.java",
        "AbcdefXyz,       7, Abc..yz",
        "A.verylongext,   8, A.very.."
    })
    void abbreviateFileName_length_middleReplaced(String fileName, int maxLength, String expected) {
        assertEquals(expected, Common.abbreviateFileName(fileName, maxLength));
    }

    @Test
    void processSLOCData_longNames_abbreviated() {
        String longPackage = "com.example.some.very.deeply.nested.package.name";
        String longFile = "AnExtremelyLongClassNameThatGoesOnAndOnForever.java";
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put(longPackage + ":" + longFile + ":src", new int[4]);

        String[] lines = Common.processSLOCData(locData, "java", false).toString().split("\n");
        String[] row = CountSLOCTest.findRow(lines, "AnExtremelyLongClas..tGoesOnAndOnForever.java");

        assertEquals("..some.very.deeply.nested.package.name", row[0]);
        assertEquals(1, Arrays.stream(lines).map(String::length).distinct().count());
    }

    @ParameterizedTest
    @CsvSource({
        "20, AnExtre..orever.java",
        "30, AnExtremelyL..ndOnForever.java",
        "60, AnExtremelyLongClassNameThatGoesOnAndOnForever.java"
    })
    void processSLOCData_maxFileNameLen_fileNameAbbreviated(int maxFileNameLen, String expected) {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:AnExtremelyLongClassNameThatGoesOnAndOnForever.java:src", new int[4]);

        String[] lines = Common.processSLOCData(locData, "java", false, Common.Border.ASCII, maxFileNameLen).toString().split("\n");

        assertEquals("a.b", CountSLOCTest.findRow(lines, expected)[0]);
    }

    @ParameterizedTest
    @CsvSource({
        "ascii,     ASCII",
        "UNICODE,   UNICODE",
        "' Unicode', UNICODE"
    })
    void borderForName_name_caseIgnored(String name, Common.Border expected) {
        assertEquals(expected, Common.Border.forName(name));
    }

    @ParameterizedTest
    @CsvSource({
        "UTF-8,        UNICODE",
        "IBM437,       UNICODE",
        "windows-1252, ASCII",
        "ISO-8859-1,   ASCII",
        "US-ASCII,     ASCII"
    })
    void borderForCharset_charset_unicodeIfEncodable(String charset, Common.Border expected) {
        assertEquals(expected, Common.Border.forCharset(Charset.forName(charset)));
    }

    @Test
    void borderForName_unknownName_exception() {
        assertThrows(IllegalArgumentException.class, () -> Common.Border.forName("double"));
    }

    @Test
    void processSLOCData_unicodeBorder_boxDrawn() {
        TreeMap<String, int[]> locData = new TreeMap<>();
        locData.put("a.b:A.java:src", new int[4]);
        locData.put("a.c:B.java:src", new int[4]);

        String[] lines = Common.processSLOCData(locData, "java", false, Common.Border.UNICODE).toString().split("\n");

        assertTrue(lines[0].matches("\u250c\u2500+(\u252c\u2500+)+\u2510"), lines[0]);
        assertTrue(lines[1].matches("\u2502.*\u2502") && !lines[1].contains("|"), lines[1]);
        assertTrue(lines[2].matches("\u251c\u2500+(\u253c\u2500+)+\u2524"), lines[2]);
        assertTrue(lines[lines.length - 1].matches("\u2514\u2500+(\u2534\u2500+)+\u2518"), lines[lines.length - 1]);
        assertEquals(1, Arrays.stream(lines).map(String::length).distinct().count());
    }

}
