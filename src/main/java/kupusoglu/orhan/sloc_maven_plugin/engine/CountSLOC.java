package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * A {@code FileVisitor} that finds Java source files, <strong>*.java</strong>, and counts lines.
 * <br>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/walk.html">Walking the File Tree</a>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Find.java</a>
 */
public class CountSLOC extends SimpleFileVisitor<Path> {
    private Log log;
    private String rootDir;
    private String fileExt;

    private PathMatcher matcherJava;
    private TreeMap<String, int[]> locData = new TreeMap<>();
    private final static int MIN_HEADER_LEN = 20;
    private final static Pattern PACKAGE_DECLARATION = Pattern.compile("(package)(\\s+)(/\\*.*\\*/)?(\\s+)?(.+)(;)");
    private final static int PACKAGE_INDEX = 5;

    private int numJavaMatch = 0;
    private boolean isblockComment = false;
    private boolean isblockDoc = false;
    private boolean isPackageFound = false;
    private String packageName;

    public CountSLOC() {
        super();
    }

    public CountSLOC(Log log, String rootDir, String fileExt) {
        this();

        this.log = log;
        this.rootDir = rootDir;
        this.fileExt = fileExt;

        this.matcherJava = FileSystems.getDefault().getPathMatcher("glob:*." + fileExt);
    }

    /**
     * If file is a source file, process it.
     *
     * @param   file    A regular file
     * @return  result  FileVisitResult
     */
    public FileVisitResult collect(Path file) {
        Path name = file.getFileName();

        if (name != null) {
            Path absolutePathOfFile = file.toAbsolutePath().normalize();

            if (matcherJava.matches(name)) {
                numJavaMatch++;
                processSource(absolutePathOfFile);
            } else {
                return FileVisitResult.CONTINUE;
            }
        }

        return FileVisitResult.CONTINUE;
    }

    public void processSource(Path absolutePath) {
        int fileCounterTotal = 0;
        int fileCounterBlank = 0;
        int fileCounterComment = 0;
        int fileCounterJavaDoc = 0;
        String trimmedLine;

        List<String> lines = Common.readTextFile(absolutePath);

        fileCounterTotal = lines.size();

        isblockComment = false;
        isblockDoc = false;
        isPackageFound = false;

        for (String line : lines) {
            trimmedLine = line.trim();

            if (isJavaDoc(trimmedLine)) {
                fileCounterJavaDoc++;
            } else if (isComment(trimmedLine)) {
                fileCounterComment++;
            } else if (isBlank(trimmedLine)) {
                fileCounterBlank++;
            } else if (!isPackageFound) {
                if (isPackage(trimmedLine)) {
                    // found package declaration
                }
            }
        }

        locData.put((packageName == null ? "" : packageName) + ":" + absolutePath.getFileName() + ":" + getFileType(absolutePath),
                    new int[]{fileCounterBlank, fileCounterJavaDoc, fileCounterComment,
                              fileCounterTotal - (fileCounterBlank + fileCounterJavaDoc + fileCounterComment)});
    }

    private boolean isBlank(String line) {
        return line.isEmpty();
    }

    private boolean isComment(String line) {
        if (line.endsWith("*/")) {
            if (this.isblockComment) {
                this.isblockComment = false;
                return true;
            } else {
                return false;
            }
        } else if (this.isblockComment) {
            return true;
        } else if (line.startsWith("//")) {
            this.isblockComment = false;
            return true;
        } else if (line.startsWith("/*") && !line.contains("*/")) {
            this.isblockComment = true;
            return true;
        } else if (line.startsWith("/*") && line.endsWith("*/")) {
            this.isblockComment = false;
            return true;
        } else {
            return false;
        }
    }

    private boolean isJavaDoc(String line) {
        if (line.endsWith("*/")) {
            if (this.isblockDoc) {
                this.isblockDoc = false;
                return true;
            } else {
                return false;
            }
        } else if (this.isblockDoc) {
            return true;
        } else if (line.startsWith("/**") && !line.contains("*/")) {
            this.isblockDoc = true;
            return true;
        } else if (line.startsWith("/**") && line.endsWith("*/")) {
            this.isblockDoc = false;
            return true;
        } else {
            return false;
        }
    }

    private boolean isPackage(String line) {
        Matcher matcher = PACKAGE_DECLARATION.matcher(line);
        isPackageFound = matcher.find();

        if (isPackageFound) {
            packageName = matcher.group(PACKAGE_INDEX);
        }

        return isPackageFound;
    }

    private String getFileType(Path path) {
        String pathStr = path.toString();

        if (pathStr.contains("/test/")) {
            return "test";
        } else if (pathStr.contains("/integration-test/")) {
            return "int-test";
        } else {
            return "src";
        }
    }

    public void done() {
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

            String commonPackage = StringUtils.getCommonPrefix(packageNames);

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

            // MIN_HEADER_LEN is longer than the minimum headers, f.ex.: "1 package(s)"
            longestPName = Math.max(MIN_HEADER_LEN, longestPName);
            longestCName = Math.max(MIN_HEADER_LEN, longestCName);

            String packageLine = packageData.size() + " package(s)";
            String classLine = locData.size() + " file(s)";

            int headerP = Math.max(longestPName - commonPackage.length(), packageLine.length());
            int headerC = Math.max(longestCName, classLine.length());

            String lineHeader = String.format("+%0" + (headerP + 2) + "d+%0" + (headerC + 2) + "d+%010d+%010d+%010d+%010d+%010d+%010d+\n",
                                              0, 0, 0, 0, 0, 0, 0, 0).replace('0', '-');

            int[] totals = new int[5];
            StringBuilder sb = new StringBuilder();

            sb.append(lineHeader);
            sb.append(String.format("| %-" + headerP + "s | %-" + headerC + "s | %-8s | %-8s | %-8s | %-8s | %-8s | %-8s |\n",
                                    "Package Name", "File Name", "Type", "Blank", "JavaDoc", "Comment", "Code", "Total"));
            sb.append(lineHeader);

            String[] fields = locData.firstKey().split(":");

            String commonPackageName = fields[0].replace(commonPackage, "");

            for (String key : keys) {
                int[] counters = locData.get(key);
                int total = IntStream.of(counters).sum();

                fields = key.split(":");

                String packageName = fields[0].replace(commonPackage, "");
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

            log.info(String.format("SLOC - directory: %s\n%s", rootDir, sb.toString()));
        } else {
            log.warn("Does not contain source files: " + rootDir + " : *." + fileExt);
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (attrs.isRegularFile()) {
            return collect(file);
        } else {
            log.warn("ignored - not a regular file: " + file);
            return FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) {
        log.error(e.getMessage());
        return FileVisitResult.CONTINUE;
    }
}
