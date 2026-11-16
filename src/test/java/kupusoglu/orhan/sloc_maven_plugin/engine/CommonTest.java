package kupusoglu.orhan.sloc_maven_plugin.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;

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
                              trimPkgNames,
                              display,
                              save);

            System.setOut(System.out);

            String[] output = baos.toString().split("\r\n|\r|\n");

            assertTrue(output.length > 0);

            String[] table = Common.removeArrayElement(output, 0);
            String actual = String.join(System.lineSeparator(), table);

            System.out.println(System.lineSeparator() + "ACTUAL OUTPUT:" + System.lineSeparator());
            System.out.println(actual + System.lineSeparator());

            String expected = String.join(System.lineSeparator(), Common.readTextFile(Paths.get(baseDir.toString(), "sloc.txt")));
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

}
