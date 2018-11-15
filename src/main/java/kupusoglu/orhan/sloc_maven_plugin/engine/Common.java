package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.nio.file.Path;
import java.util.*;


/**
 * Common utilities of the plugin
 */
public class Common {
    private Common() {
        // no instance required, use static factory methods
    }

    public static List<String> readTextFile(Path absolutePath) {
        File file = new File(absolutePath.toString());
        List<String> lines = new ArrayList<>();

        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                InputStream inputStream = new FileInputStream(file);

                try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
                ) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch(FileNotFoundException e2){
                e2.printStackTrace();
            }
        }

        return lines;
    }

    public static void countLines(Log log, String rootDir, String srcMain, String fileExt, boolean trimPkgNames)
            throws MojoExecutionException {
        CountLines countLines = new CountLines(log, rootDir, srcMain, fileExt, trimPkgNames);
        countLines.execute();
    }

    public static String trimPackageName(String packageName) {
        int last = packageName.lastIndexOf(".");
        return packageName.substring(0, last + 1);
    }

    public static String[] removeArrayElement(String[] src, int index) {
        List<String> dst = new LinkedList<String>(Arrays.asList(src));
        dst.remove(index);
        return dst.toArray(new String[src.length - 1]);
    }
}
