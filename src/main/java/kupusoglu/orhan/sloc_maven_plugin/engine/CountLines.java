package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Counts the source lines below a source directory.
 */
public class CountLines {
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

    /**
     * Creates an empty instance.
     */
    public CountLines() {
        super();
    }

    /**
     * Creates a counter for the given settings.
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
    public CountLines(Log log, String baseDir, String srcMain, String fileExt, Charset charset, boolean trimPkgNames, Common.Border border, int maxFileNameLen, boolean display, boolean save) {
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
    }

    /**
     * Walks the source directory and displays or saves the table.
     *
     * @throws MojoExecutionException if counting fails
     */
    public void execute() throws MojoExecutionException {
        try {
            if (Files.exists(Paths.get(baseDir + File.separator + srcMain).toAbsolutePath())) {
                CountSLOC countSLOC = new CountSLOC(log, baseDir, srcMain, fileExt, charset, trimPkgNames, border, maxFileNameLen, display, save);

                Files.walkFileTree(Paths.get(baseDir + File.separator + srcMain).toAbsolutePath(), countSLOC);
                countSLOC.done();
            } else {
                log.warn("Does not contain a source directory: " + baseDir  + File.separator + srcMain);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
