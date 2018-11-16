package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CountLines {
    private Log log;
    private String baseDir;
    private String srcMain;
    private String fileExt;
    private boolean trimPkgNames;
    private boolean display;
    private boolean save;


    public CountLines() {
        super();
    }

    public CountLines(Log log, String baseDir, String srcMain, String fileExt, boolean trimPkgNames, boolean display, boolean save) {
        this();

        this.log = log;
        this.baseDir = baseDir;
        this.srcMain = srcMain;
        this.fileExt = fileExt;
        this.trimPkgNames = trimPkgNames;
        this.display = display;
        this.save = save;
    }

    public void execute() throws MojoExecutionException {
        try {
            if (Files.exists(Paths.get(baseDir + File.separator + srcMain).toAbsolutePath())) {
                CountSLOC countSLOC = new CountSLOC(log, baseDir, srcMain, fileExt, trimPkgNames, display, save);

                java.nio.file.Files.walkFileTree(Paths.get(baseDir + File.separator + srcMain).toAbsolutePath(), countSLOC);
                countSLOC.done();
            } else {
                log.warn("Does not contain a source directory: " + baseDir  + File.separator + srcMain);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
