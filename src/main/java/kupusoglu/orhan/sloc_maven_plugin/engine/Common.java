package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Common utilities of the plugin
 */
public final class Common {
    public static final int MIN_HEADER_LEN = 16;
    // longer package and file names are abbreviated
    public static final int MAX_PKG_NAME_LEN = 40;
    public static final int DEFAULT_MAX_FILE_NAME_LEN = 45;
    // the semicolon is optional in Groovy, the only group is the package name
    public static final Pattern PACKAGE_DECLARATION = Pattern.compile("\\bpackage\\s+(?:/\\*.*?\\*/\\s*)?([\\p{javaJavaIdentifierPart}.]+)");
    public static final String OUTPUT_SLOC_FILE = "sloc.txt";

    /**
     * Characters to draw the table border with.
     */
    public enum Border {
        ASCII('-', '|', "+++++++++"),
        UNICODE('\u2500', '\u2502', "\u250c\u252c\u2510\u251c\u253c\u2524\u2514\u2534\u2518");

        private final char   horizontal;
        private final char   vertical;
        // left, inner and right junction of the top, middle and bottom line
        private final String junctions;

        /**
         * Creates a border of the given characters.
         *
         * @param horizontal  character of the horizontal lines
         * @param vertical    character of the vertical lines
         * @param junctions   left, inner and right junction of the top, middle and bottom line
         */
        Border(char horizontal, char vertical, String junctions) {
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.junctions = junctions;
        }

        /**
         * Returns the border of the given name, ignoring case. The name auto selects the border
         * that the console can display.
         *
         * @param  name  border name: auto, ascii or unicode
         * @return the border
         * @throws IllegalArgumentException if there is no border of that name
         */
        public static Border forName(String name) {
            if ("auto".equalsIgnoreCase(name.trim())) {
                return forCharset(consoleCharset());
            }

            for (Border border : values()) {
                if (border.name().equalsIgnoreCase(name.trim())) {
                    return border;
                }
            }
            throw new IllegalArgumentException(String.format("Unsupported border: %s, use auto, ascii or unicode", name));
        }

        /**
         * Returns the unicode border if the given charset can encode its characters, the ascii border otherwise.
         *
         * @param  charset  charset of the output
         * @return the border
         */
        public static Border forCharset(Charset charset) {
            String characters = UNICODE.horizontal + "" + UNICODE.vertical + UNICODE.junctions;
            return charset.canEncode() && charset.newEncoder().canEncode(characters) ? UNICODE : ASCII;
        }

        /**
         * Returns the charset of the standard output, as far as the running Java version tells it.
         */
        private static Charset consoleCharset() {
            // stdout.encoding exists since Java 19, sun.stdout.encoding on Windows consoles before
            for (String property : new String[] {"stdout.encoding", "sun.stdout.encoding", "native.encoding"}) {
                String name = System.getProperty(property);
                try {
                    if (name != null && Charset.isSupported(name)) {
                        return Charset.forName(name);
                    }
                } catch (IllegalArgumentException ignored) {
                    // an illegal charset name, try the next property
                }
            }
            return Charset.defaultCharset();
        }

        /**
         * Returns a horizontal line of the table, terminated by a line break.
         *
         * @param position  0 for the top line, 1 for a line inside the table, 2 for the bottom line
         * @param widths    widths of the column contents
         */
        String line(int position, int... widths) {
            String left = String.valueOf(junctions.charAt(3 * position));
            String inner = String.valueOf(junctions.charAt(3 * position + 1));
            String right = junctions.charAt(3 * position + 2) + "\n";
            StringJoiner line = new StringJoiner(inner, left, right);

            for (int width : widths) {
                // one blank on either side of the content
                line.add(new String(new char[width + 2]).replace('\0', horizontal));
            }

            return line.toString();
        }

        /**
         * Returns the row format with the vertical lines of this border.
         */
        String row(String format) {
            return format.replace('|', vertical);
        }
    }

    private Common() {
        // no instance required, use static factory methods
    }

    /**
     * Reads all lines of a text file, an empty list if the file cannot be read.
     *
     * @param  absolutePath  path of the file
     * @param  charset       charset of the file
     * @return the lines of the file
     */
    public static List<String> readTextFile(Path absolutePath, Charset charset) {
        File file = new File(absolutePath.toString());
        List<String> lines = new ArrayList<>();

        if (file.exists() && file.isFile() && file.canRead()) {
            try {
                InputStream inputStream = new FileInputStream(file);

                try (
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))
                ) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        return lines;
    }

    /**
     * Counts the source lines below the given directory and displays or saves the table.
     *
     * @param  log             log to write to
     * @param  rootDir         base directory of the project
     * @param  srcMain         source directory, relative to the base directory
     * @param  fileExt         extension of the source files
     * @param  charset         charset of the source files
     * @param  trimPkgNames    whether to remove the common package name
     * @param  border          border of the table
     * @param  maxFileNameLen  file names longer than this are abbreviated
     * @param  display         whether to log the table
     * @param  save            whether to save the table to a file
     * @throws MojoExecutionException if counting fails
     */
    public static void countLines(Log log, String rootDir, String srcMain, String fileExt, Charset charset, boolean trimPkgNames, Border border,
            int maxFileNameLen, boolean display, boolean save) throws MojoExecutionException {
        CountLines countLines = new CountLines(log, rootDir, srcMain, fileExt, charset, trimPkgNames, border, maxFileNameLen, display, save);
        countLines.execute();
    }

    /**
     * Returns the package name without its last segment, the trailing dot kept.
     *
     * @param  packageName  package name
     * @return the trimmed package name
     */
    public static String trimPackageName(String packageName) {
        int last = packageName.lastIndexOf(".");
        return packageName.substring(0, last + 1);
    }

    /**
     * Returns the package name as displayed in the table: the common package name removed,
     * or the full package name if it does not start with the common package name.
     *
     * @param  packageName    package name
     * @param  commonPackage  common package name, including the trailing dot
     * @return the package name to display
     */
    public static String displayPackageName(String packageName, String commonPackage) {
        return packageName.startsWith(commonPackage) ? packageName.substring(commonPackage.length()) : packageName;
    }

    /**
     * Returns the package name, abbreviated to the given length by replacing its leading segments with "..".
     * The end of a package name tells the packages apart. A last segment too long by itself is cut anywhere.
     *
     * @param  packageName  package name
     * @param  maxLength    maximum length
     * @return the abbreviated package name
     */
    public static String abbreviatePackageName(String packageName, int maxLength) {
        if (packageName.length() <= maxLength) {
            return packageName;
        }

        int start = packageName.length() - maxLength + 2;
        int dot = packageName.indexOf('.', start - 1);

        return ".." + packageName.substring(dot < 0 ? start : dot + 1);
    }

    /**
     * Returns the file name, abbreviated to the given length by replacing the middle of its base name with "..".
     * Start and end of the base name tell the files apart, the extension is kept.
     *
     * @param  fileName   file name
     * @param  maxLength  maximum length
     * @return the abbreviated file name
     */
    public static String abbreviateFileName(String fileName, int maxLength) {
        if (fileName.length() <= maxLength) {
            return fileName;
        }

        int dot = fileName.lastIndexOf('.');
        String extension = dot > 0 ? fileName.substring(dot) : "";
        String baseName = fileName.substring(0, fileName.length() - extension.length());
        int kept = maxLength - extension.length() - 2;

        if (kept < 2) {
            return fileName.substring(0, maxLength - 2) + "..";
        }

        return baseName.substring(0, kept - kept / 2) + ".." + baseName.substring(baseName.length() - kept / 2) + extension;
    }

    /**
     * Returns the count followed by the noun, in plural unless the count is one.
     *
     * @param  count  count
     * @param  noun   noun in singular
     * @return count and noun
     */
    public static String countNoun(int count, String noun) {
        return count + " " + noun + (count == 1 ? "" : "s");
    }

    /**
     * Returns a copy of the array without the element at the given index.
     *
     * @param  src    source array
     * @param  index  index of the element to remove
     * @return the new array
     */
    public static String[] removeArrayElement(String[] src, int index) {
        List<String> dst = new LinkedList<String>(Arrays.asList(src));
        dst.remove(index);
        return dst.toArray(new String[src.length - 1]);
    }

    /**
     * Returns the SLOC table of the given data, drawn with the ascii border.
     *
     * @param  locData       counters by package, file name and type
     * @param  fileExt       extension of the source files
     * @param  trimPkgNames  whether to remove the common package name
     * @return the table
     */
    public static StringBuilder processSLOCData(TreeMap<String, int[]> locData, String fileExt, boolean trimPkgNames) {
        return processSLOCData(locData, fileExt, trimPkgNames, Border.ASCII);
    }

    /**
     * Returns the SLOC table of the given data, drawn with the given border.
     *
     * @param  locData       counters by package, file name and type
     * @param  fileExt       extension of the source files
     * @param  trimPkgNames  whether to remove the common package name
     * @param  border        border of the table
     * @return the table
     */
    public static StringBuilder processSLOCData(TreeMap<String, int[]> locData, String fileExt, boolean trimPkgNames, Border border) {
        return processSLOCData(locData, fileExt, trimPkgNames, border, DEFAULT_MAX_FILE_NAME_LEN);
    }

    /**
     * Returns the SLOC table of the given data, drawn with the given border, longer file names abbreviated.
     *
     * @param  locData         counters by package, file name and type
     * @param  fileExt         extension of the source files
     * @param  trimPkgNames    whether to remove the common package name
     * @param  border          border of the table
     * @param  maxFileNameLen  file names longer than this are abbreviated
     * @return the table
     */
    public static StringBuilder processSLOCData(TreeMap<String, int[]> locData, String fileExt, boolean trimPkgNames, Border border, int maxFileNameLen) {
        StringBuilder sb = new StringBuilder();

        if (locData.size() > 0) {
            // sort by package first, a plain string sort would put "a.b.c:X.java" before "a.b:Y.java"
            SortedSet<String> keys = new TreeSet<>(Comparator.comparing((String key) -> key.split(":")[0])
                    .thenComparing(key -> key.substring(key.indexOf(':') + 1)));
            keys.addAll(locData.keySet());

            Set<String> packageData = new TreeSet<>();
            int longestCName = 0;
            int longestType = 0;
            int grandTotal = 0;
            boolean fileNamesShortened = false;

            for (String key : keys) {
                String[] fields = key.split(":");

                String packageName = fields[0];
                String className = abbreviateFileName(fields[1], maxFileNameLen);

                packageData.add(packageName);

                fileNamesShortened |= !className.equals(fields[1]);
                longestCName = Math.max(className.length(), longestCName);
                longestType = Math.max(fields[2].length(), longestType);
                grandTotal += IntStream.of(locData.get(key)).sum();
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
                longestPNameDisplayed = Math.max(abbreviatePackageName(displayPackageName(packageName, commonPackage), MAX_PKG_NAME_LEN).length(), longestPNameDisplayed);
            }

            String packageLine = countNoun(packageData.size(), "package");
            String classLine = countNoun(locData.size(), "file");

            // when the package names are shortened, the header marks them as such
            String packageHeader = trimPkgNames && !commonPackage.isEmpty() ? "Short Package Name" : "Package Name";
            // "Short File Name" is shorter than MIN_HEADER_LEN, so it does not widen the column
            String fileHeader = fileNamesShortened ? "Short File Name" : "File Name";

            int headerP = Math.max(Math.max(longestPNameDisplayed, packageLine.length()), packageHeader.length());
            int headerC = Math.max(longestCName, classLine.length());

            // MIN_HEADER_LEN is longer than the shortest footers: "1 package" & "1 file"
            headerP = Math.max(MIN_HEADER_LEN, headerP);
            headerC = Math.max(MIN_HEADER_LEN, headerC);

            int headerT = Math.max(Math.max(longestType, fileExt.length()), "Type".length());

            // the number columns share one width, wide enough for the longest header and the grand total
            int headerN = Math.max("Javadoc".length(), String.valueOf(grandTotal).length());

            int[] widths = {headerP, headerC, headerT, headerN, headerN, headerN, headerN, headerN};
            String lineTop = border.line(0, widths);
            String lineMiddle = border.line(1, widths);
            String lineBottom = border.line(2, widths);

            // the numbers are right-aligned like their headers, %s formats them like %d
            String numbers = String.join("", Collections.nCopies(5, " | %" + headerN + "s"));
            String row = border.row("| %-" + headerP + "s | %-" + headerC + "s | %-" + headerT + "s" + numbers + " |\n");

            int[] totals = new int[5];

            sb.append(lineTop)
                    .append(String.format(row, packageHeader, fileHeader, "Type", "Blank", "Javadoc", "Comment", "Code", "Total"))
                    .append(lineMiddle);

            String[] fields = keys.first().split(":");

            String commonPackageName = displayPackageName(fields[0], commonPackage);

            for (String key : keys) {
                int[] counters = locData.get(key);
                int total = IntStream.of(counters).sum();

                fields = key.split(":");

                String packageName = displayPackageName(fields[0], commonPackage);
                String className = abbreviateFileName(fields[1], maxFileNameLen);
                String classType = fields[2];

                if (!packageName.equals(commonPackageName)) {
                    sb.append(lineMiddle);
                    commonPackageName = packageName;
                }

                sb.append(String.format(row, abbreviatePackageName(packageName, MAX_PKG_NAME_LEN), className, classType, counters[0], counters[1], counters[2], counters[3], total));

                totals[0] += counters[0];
                totals[1] += counters[1];
                totals[2] += counters[2];
                totals[3] += counters[3];
                totals[4] += total;
            }

            sb.append(lineMiddle)
                    .append(String.format(row, packageLine, classLine, fileExt, totals[0], totals[1], totals[2], totals[3], totals[4]))
                    .append(lineBottom);
        }

        return sb;
    }

    /**
     * Returns the package name prefix that is shared by the most packages, the longest such prefix on equal counts.
     * A package that shares no prefix with the others, such as an unrelated root or the default package,
     * therefore does not disable the trimming, it is merely displayed in full.
     * Returns an empty string if no prefix is shared by at least two packages.
     *
     * @param  packages  package names
     * @return the common package name prefix, null if there are no packages
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
