package kupusoglu.orhan.sloc_maven_plugin.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    }

}
