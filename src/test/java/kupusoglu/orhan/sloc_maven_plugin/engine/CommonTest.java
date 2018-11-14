package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CommonTest extends AbstractMojo {
    @Test
    public void testPackageName() throws Exception {
        final String packageName = "sampleapp.cli.commands";
        final String expected = "sampleapp.cli.";
        final String actual = Common.trimPackageName(packageName);

        Assert.assertEquals("Trim package name - failure", expected, actual);
    }

    @Test
    public void testCountLines() {
        Path baseDir = Paths.get(new File(".").getAbsolutePath().toString(), "junit-test").normalize();
        Path resDir = Paths.get(new File(".").getAbsolutePath().toString(), "src/test/resources").normalize();
        String srcMain = "src";
        String fileExt = "java";

        try {
            // capture output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream stdOut = System.out;
            System.setOut(ps);

            Common.countLines(getLog(),
                              baseDir.toString(),
                              srcMain,
                              fileExt);

            System.setOut(stdOut);

            String[] output = baos.toString().split("\n");

            if (output.length > 0) {
                String[] table = Common.removeArrayElement(output, 0);
                String actual = String.join("\n", table);

                System.out.println("\nACTUAL OUTPUT:\n");
                System.out.println(actual + "\n");

                String expected = String.join("\n", Common.readTextFile(Paths.get(resDir.toString(),"table.txt")));
                System.out.println("EXPECTED OUTPUT:\n");
                System.out.println(expected + "\n");

                Assert.assertEquals("SLOC - failure", expected, actual);
            } else {
                Assert.fail();
            }
        } catch (MojoExecutionException e) {
            getLog().error(e.getMessage());
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // not used
    }
}
