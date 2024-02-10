package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Common utilities of the plugin
 */
public class Common {
    public final static int MIN_HEADER_LEN = 16;
    public final static Pattern PACKAGE_DECLARATION = Pattern.compile("(package)(\\s+)(/\\*.*\\*/)?(\\s+)?(.+)(;)");
    public final static int PACKAGE_INDEX = 5;
    public final static String OUTPUT_SLOC_FILE = "sloc.txt";

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

    public static void countLines(Log log, String rootDir, String srcMain, String fileExt, boolean trimPkgNames, boolean display, boolean save)
            throws MojoExecutionException {
        CountLines countLines = new CountLines(log, rootDir, srcMain, fileExt, trimPkgNames, display, save);
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

    public static StringBuilder processSLOCData(TreeMap<String, int[]> locData, String fileExt, boolean trimPkgNames) {
        StringBuilder sb = new StringBuilder();

        if (locData.size() > 0) {
            SortedSet<String> keys = new TreeSet<>(locData.keySet());

            Set<String> packageData = new TreeSet<>();
            int longestPName = 0;
            int longestCName = 0;

            for (String key : keys) {
                String[] fields = key.split(":");

                String packageName = fields[0];
                String className = fields[1];

                packageData.add(packageName);

                longestPName = Math.max(packageName.length(), longestPName);
                longestCName = Math.max(className.length(), longestCName);
            }

            String[] packageNames = packageData.stream()
                    .toArray(String[]::new);

            String commonPackage = "";

            if (trimPkgNames) {
                commonPackage = getCommonPackagePrefix(packageNames);

                // if all package names are identical, trim the last part of the common package name
                if (commonPackage.length() == longestPName) {
                    commonPackage = Common.trimPackageName(commonPackage);
                }

                // if some packagae names will be eliminated completely, trim the last part of the common package name
                final String tempCommonPackage = commonPackage;
                Optional<String> result = Arrays.stream(packageNames)
                        .filter(s -> s.replace(tempCommonPackage, "").isEmpty())
                        .findAny();

                if (result.isPresent()) {
                    commonPackage = Common.trimPackageName(commonPackage);
                }
            }

            String packageLine = packageData.size() + " package(s)";
            String classLine = locData.size() + " file(s)";

            int headerP = Math.max(longestPName - commonPackage.length(), packageLine.length());
            int headerC = Math.max(longestCName, classLine.length());

            // MIN_HEADER_LEN is longer than the minimum headers.: "1 package(s)" & "1 file(s)"
            headerP = Math.max(MIN_HEADER_LEN, headerP);
            headerC = Math.max(MIN_HEADER_LEN, headerC);

            String lineHeader = String.format("+%0" + (headerP + 2) + "d+%0" + (headerC + 2) + "d+%010d+%010d+%010d+%010d+%010d+%010d+\n",
                    0, 0, 0, 0, 0, 0, 0, 0).replace('0', '-');

            int[] totals = new int[5];

            sb.append(lineHeader);
            sb.append(String.format("| %-" + headerP + "s | %-" + headerC + "s | %-8s | %-8s | %-8s | %-8s | %-8s | %-8s |\n",
                    "Package Name", "File Name", "Type", "Blank", "JavaDoc", "Comment", "Code", "Total"));
            sb.append(lineHeader);

            String[] fields = locData.firstKey().split(":");

            String commonPackageName = trimPkgNames ? fields[0].replace(commonPackage, "") : fields[0];

            for (String key : keys) {
                int[] counters = locData.get(key);
                int total = IntStream.of(counters).sum();

                fields = key.split(":");

                String packageName = trimPkgNames ? fields[0].replace(commonPackage, "") : fields[0];
                String className = fields[1];
                String classType = fields[2];

                if (!packageName.equals(commonPackageName)) {
                    sb.append(lineHeader);
                    commonPackageName = packageName;
                }

                sb.append(String.format("| %-" + headerP + "s | %-" + headerC + "s | %-8s | %8d | %8d | %8d | %8d | %8d |\n",
                        packageName, className, classType, counters[0], counters[1], counters[2], counters[3], total));

                totals[0] += counters[0];
                totals[1] += counters[1];
                totals[2] += counters[2];
                totals[3] += counters[3];
                totals[4] += total;
            }

            sb.append(lineHeader);
            sb.append(String.format("| %-" + headerP + "s | %-" + headerC + "s | %-8s | %8d | %8d | %8d | %8d | %8d |\n",
                    packageLine, classLine, fileExt, totals[0], totals[1], totals[2], totals[3], totals[4]));
            sb.append(lineHeader);
        }

        return sb;
    }

    public static String getCommonPackagePrefix(String[] packages) {
        if (packages == null || packages.length == 0) {
            return null;
        } else if (packages.length == 1) {
            return packages[0];
        }
        List<String> list = Arrays.stream(packages).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        String firstPackage = list.remove(0);
        for (int i = 0; i < firstPackage.length(); i++) {
            char c = firstPackage.charAt(i);
            for (String pack : list) {
                if (i == pack.length() || pack.charAt(i) != c) {
                    return firstPackage.substring(0, i);
                }
            }
        }
        return firstPackage;
    }

}
