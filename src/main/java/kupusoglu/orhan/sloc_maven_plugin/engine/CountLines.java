package kupusoglu.orhan.sloc_maven_plugin.engine;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CountLines {
    private Log log;
    private String rootDir;
    private String srcMain;
    private String fileExt;
    private boolean trimPkgNames;

    public CountLines() {
        super();
    }

    public CountLines(Log log, String rootDir, String srcMain, String fileExt, boolean trimPkgNames) {
        this();

        this.log = log;
        this.rootDir = rootDir;
        this.srcMain = srcMain;
        this.fileExt = fileExt;
        this.trimPkgNames = trimPkgNames;
    }

    public void execute() throws MojoExecutionException {
        try {
            if (Files.exists(Paths.get(rootDir + File.separator + srcMain).toAbsolutePath())) {
                CountSLOC countSLOC = new CountSLOC(log, rootDir + File.separator + srcMain, fileExt, trimPkgNames);

                java.nio.file.Files.walkFileTree(Paths.get(rootDir + File.separator + srcMain).toAbsolutePath(), countSLOC);
                countSLOC.done();
            } else {
                log.warn("Does not contain a source directory: " + rootDir  + File.separator + srcMain);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
