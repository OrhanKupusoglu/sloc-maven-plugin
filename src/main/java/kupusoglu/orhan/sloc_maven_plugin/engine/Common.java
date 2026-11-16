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

    /**
     * Returns the package name as displayed in the table: the common package name removed,
     * or the full package name if it does not start with the common package name.
     */
    public static String displayPackageName(String packageName, String commonPackage) {
        return packageName.startsWith(commonPackage) ? packageName.substring(commonPackage.length()) : packageName;
    }

    public static String[] removeArrayElement(String[] src, int index) {
        List<String> dst = new LinkedList<String>(Arrays.asList(src));
        dst.remove(index);
        return dst.toArray(new String[src.length - 1]);
    }

    public static StringBuilder processSLOCData(TreeMap<String, int[]> locData, String fileExt, boolean trimPkgNames) {
        StringBuilder sb = new StringBuilder();

        if (locData.size() > 0) {
            // sort by package first, a plain string sort would put "a.b.c:X.java" before "a.b:Y.java"
            SortedSet<String> keys = new TreeSet<>(Comparator.comparing((String key) -> key.split(":")[0])
                    .thenComparing(key -> key.substring(key.indexOf(':') + 1)));
            keys.addAll(locData.keySet());

            Set<String> packageData = new TreeSet<>();
            int longestCName = 0;

            for (String key : keys) {
                String[] fields = key.split(":");

                String packageName = fields[0];
                String className = fields[1];

                packageData.add(packageName);

                longestCName = Math.max(className.length(), longestCName);
            }

            String[] packageNames = packageData.stream()
                    .toArray(String[]::new);

            String commonPackage = "";

            if (trimPkgNames) {
                commonPackage = getCommonPackagePrefix(packageNames);

                // if some package names would be eliminated completely, trim the last part of the common package name
                if (packageData.contains(commonPackage)) {
                    commonPackage = Common.trimPackageName(commonPackage);
                } else if (!commonPackage.isEmpty()) {
                    commonPackage += ".";
                }
            }

            // packages that do not start with the common package name are displayed in full
            int longestPNameDisplayed = 0;

            for (String packageName : packageData) {
                longestPNameDisplayed = Math.max(displayPackageName(packageName, commonPackage).length(), longestPNameDisplayed);
            }

            String packageLine = packageData.size() + " package(s)";
            String classLine = locData.size() + " file(s)";

            int headerP = Math.max(longestPNameDisplayed, packageLine.length());
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

            String[] fields = keys.first().split(":");

            String commonPackageName = displayPackageName(fields[0], commonPackage);

            for (String key : keys) {
                int[] counters = locData.get(key);
                int total = IntStream.of(counters).sum();

                fields = key.split(":");

                String packageName = displayPackageName(fields[0], commonPackage);
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

    /**
     * Returns the package name prefix that is shared by the most packages, the longest such prefix on equal counts.
     * A package that shares no prefix with the others, such as an unrelated root or the default package,
     * therefore does not disable the trimming, it is merely displayed in full.
     * Returns an empty string if no prefix is shared by at least two packages.
     */
    public static String getCommonPackagePrefix(String[] packages) {
        if (packages == null || packages.length == 0) {
            return null;
        }

        List<String> list = Arrays.stream(packages).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        if (list.isEmpty()) {
            return "";
        } else if (list.size() == 1) {
            return list.get(0);
        }

        String commonPackage = "";
        int mostPackages = 2;

        for (String pack : list) {
            // try every whole-segment prefix of the package, the package name itself included
            for (int dot = pack.indexOf('.'); ; dot = pack.indexOf('.', dot + 1)) {
                String prefix = dot < 0 ? pack : pack.substring(0, dot);
                int packagesCovered = 0;

                for (String other : list) {
                    // compare whole segments, so that "a.b" and "a.bc" share "a" and not "a.b"
                    if (other.equals(prefix) || other.startsWith(prefix + ".")) {
                        packagesCovered++;
                    }
                }

                if (packagesCovered > mostPackages || (packagesCovered == mostPackages && prefix.length() > commonPackage.length())) {
                    commonPackage = prefix;
                    mostPackages = packagesCovered;
                }

                if (dot < 0) {
                    break;
                }
            }
        }

        return commonPackage;
    }

}
