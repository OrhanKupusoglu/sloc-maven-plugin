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
