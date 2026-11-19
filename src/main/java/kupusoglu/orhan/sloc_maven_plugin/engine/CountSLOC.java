package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    private Charset charset;
    private boolean trimPkgNames;
    private Common.Border border;
    private int maxFileNameLen;
    private boolean display;
    private boolean save;

    private PathMatcher matcherJava;
    private final Map<String, int[]> locData = new TreeMap<>();
    private boolean isblockComment = false;
    private boolean isblockDoc = false;

    /**
     * Creates an empty instance.
     */
    public CountSLOC() {
        super();
    }

    /**
     * Creates a file visitor for the given settings.
     *
     * @param  log             log to write to
     * @param  baseDir         base directory of the project
     * @param  srcMain         source directory, relative to the base directory
     * @param  fileExt         extension of the source files
     * @param  charset         charset of the source files
     * @param  trimPkgNames    whether to remove the common package name
     * @param  border          border of the table
     * @param  maxFileNameLen  file names longer than this are abbreviated
     * @param  display         whether to log the table
     * @param  save            whether to save the table to a file
     */
    public CountSLOC(Log log, String baseDir, String srcMain, String fileExt, Charset charset, boolean trimPkgNames, Common.Border border, int maxFileNameLen, boolean display, boolean save) {
        this();

        this.log = log;
        this.baseDir = baseDir;
        this.srcMain = srcMain;
        this.fileExt = fileExt;
        this.charset = charset;
        this.trimPkgNames = trimPkgNames;
        this.border = border;
        this.maxFileNameLen = maxFileNameLen;
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

    /**
     * Counts the lines of a source file and stores the counters.
     *
     * @param  absolutePath  path of the source file
     */
    public void processSource(Path absolutePath) {
        List<String> lines;
        try {
            lines = Common.readTextFile(absolutePath, charset);
        } catch (IOException ex) {
            log.error(String.format("Cannot read %s: %s", absolutePath, ex.getMessage()));
            return;
        }

        int fileCounterTotal = lines.size();
        int fileCounterBlank = 0;
        int fileCounterComment = 0;
        int fileCounterJavaDoc = 0;
        String packageName = null;

        isblockComment = false;
        isblockDoc = false;

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (isJavaDoc(trimmedLine)) {
                fileCounterJavaDoc++;
            } else if (isComment(trimmedLine)) {
                fileCounterComment++;
            } else if (isBlank(trimmedLine)) {
                fileCounterBlank++;
            } else if (packageName == null) {
                packageName = findPackageName(trimmedLine);
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
        if (this.isblockComment) {
            if (line.endsWith("*/")) {
                this.isblockComment = false;
            }
            return true;
        } else if (line.startsWith("//")) {
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
        if (this.isblockDoc) {
            if (line.endsWith("*/")) {
                this.isblockDoc = false;
            }
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

    /**
     * Returns the package name declared in the line, null if there is none.
     */
    private String findPackageName(String line) {
        Matcher matcher = Common.PACKAGE_DECLARATION.matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String getFileType(Path path) {
        // only look at directories below the source root, the project itself may live in a directory named "test"
        Path srcRoot = Paths.get(baseDir, srcMain).toAbsolutePath().normalize();
        Path relativeDir = path.startsWith(srcRoot) ? srcRoot.relativize(path).getParent() : path.getParent();
        List<String> dirNames = new ArrayList<>();
        if (relativeDir != null) {
            relativeDir.forEach(dir -> dirNames.add(dir.toString()));
        }

        if (dirNames.contains("test")) {
            return "test";
        } else if (dirNames.contains("integration-test")) {
            return "int-test";
        } else {
            return "src";
        }
    }

    /**
     * Displays or saves the table of the counted files.
     */
    public void done() {
        String data = Common.processSLOCData(locData, fileExt, trimPkgNames, border, maxFileNameLen).toString();

        if (display) {
            if (data.length() > 0) {
                log.info(String.format("SLOC - directory: %s\n%s", (baseDir + File.separator + srcMain), data));
            } else {
                log.warn("Does not contain source files: " + (baseDir + File.separator + srcMain) + " : *." + fileExt);
            }
        }

        if (save) {
            File file = new File(baseDir + File.separator + Common.OUTPUT_SLOC_FILE);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.append(data);
            } catch (IOException ex) {
                log.error(ex.getMessage());
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
