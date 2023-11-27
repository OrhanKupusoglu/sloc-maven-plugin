package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;


/**
 * A {@code FileVisitor} that finds Java source files, <strong>*.java</strong>, and counts lines.
 * <br>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/walk.html">Walking the File Tree</a>
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Find.java</a>
 */
public class CountSLOC extends SimpleFileVisitor<Path> {
    private Log log;
    private String baseDir;
    private String srcMain;
    private String fileExt;
    private boolean trimPkgNames;
    private boolean display;
    private boolean save;

    private PathMatcher matcherJava;
    private TreeMap<String, int[]> locData = new TreeMap<>();
    private boolean isblockComment = false;
    private boolean isblockDoc = false;
    private boolean isPackageFound = false;
    private String packageName;

    public CountSLOC() {
        super();
    }

    public CountSLOC(Log log, String baseDir, String srcMain, String fileExt, boolean trimPkgNames, boolean display, boolean save) {
        this();

        this.log = log;
        this.baseDir = baseDir;
        this.srcMain = srcMain;
        this.fileExt = fileExt;
        this.trimPkgNames = trimPkgNames;
        this.display = display;
        this.save = save;

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
        Matcher matcher = Common.PACKAGE_DECLARATION.matcher(line);
        isPackageFound = matcher.find();

        if (isPackageFound) {
            packageName = matcher.group(Common.PACKAGE_INDEX);
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
        String data = Common.processSLOCData(locData, fileExt, trimPkgNames).toString();

        if (display) {
            if (data.length() > 0) {
                log.info(String.format("SLOC - directory: %s\n%s", (baseDir + File.separator + srcMain), data));
            } else {
                log.warn("Does not contain source files: " + (baseDir + File.separator + srcMain) + " : *." + fileExt);
            }
        }

        if (save) {
            File file = new File(baseDir + File.separator + Common.OUTPUT_SLOC_FILE);
            FileWriter writer = null;

            try {
                writer = new FileWriter(file);
                writer.append(data);
                writer.flush();
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
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
