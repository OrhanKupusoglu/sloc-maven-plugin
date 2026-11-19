package kupusoglu.orhan.sloc_maven_plugin.goal;

import kupusoglu.orhan.sloc_maven_plugin.engine.Common;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Goal <strong>loc</strong>
 * <br>
 * Count source line of codes
 * <br>
 * @see <a href="https://en.wikipedia.org/wiki/Source_lines_of_code">Source lines of code - Wikipedia</a>
 *
 * <br><br>
 * <pre>
 * mvn kupusoglu.orhan:sloc-maven-plugin:sloc
 * mvn sloc:sloc
 * </pre>
 */
@Mojo(
    name = "sloc",
    defaultPhase = LifecyclePhase.VALIDATE
)
public class GoalSLOC extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * name of the root directory for the source files
     * @parameter
     */
    @Parameter(property = "srcMain", defaultValue = "src")
    private String srcMain;

    /**
     * extension of the source files
     * @parameter
     */
    @Parameter(property = "fileExt", defaultValue = "java")
    private String fileExt;

    /**
     * encoding of the source files, UTF-8 if not set
     * @parameter
     */
    @Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
    private String encoding;

    /**
     * trim package names
     * @parameter
     */
    @Parameter(property = "trimPkgNames", defaultValue = "false")
    private boolean trimPkgNames;

    /**
     * table border, ascii or unicode, auto selects unicode if the console can display it
     * @parameter
     */
    @Parameter(property = "border", defaultValue = "auto")
    private String border;

    /**
     * abbreviate file names longer than this, at least 16
     * @parameter
     */
    @Parameter(property = "maxFileNameLen", defaultValue = "45")
    private int maxFileNameLen;

    /**
     * output SLOC data to stdout
     * @parameter
     */
    @Parameter(property = "display", defaultValue = "true")
    private boolean display;

    /**
     * output SLOC data to file "sloc.txt"
     * @parameter
     */
    @Parameter(property = "save", defaultValue = "false")
    private boolean save;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Common.countLines(getLog(),
                              project.getBasedir().getAbsolutePath(),
                              srcMain,
                              fileExt,
                              getCharset(),
                              trimPkgNames,
                              getBorder(),
                              getMaxFileNameLen(),
                              display,
                              save);
        } catch (MojoExecutionException ex) {
            getLog().error(ex.getMessage());
        }
    }

    /**
     * Returns the configured table border.
     */
    private Common.Border getBorder() throws MojoExecutionException {
        try {
            return Common.Border.forName(border);
        } catch (IllegalArgumentException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * Returns the configured maximum length of the file names.
     */
    private int getMaxFileNameLen() throws MojoExecutionException {
        if (maxFileNameLen < Common.MIN_HEADER_LEN) {
            throw new MojoExecutionException(String.format("maxFileNameLen must be at least %d: %d", Common.MIN_HEADER_LEN, maxFileNameLen));
        }
        return maxFileNameLen;
    }

    /**
     * Returns the charset of the configured encoding, UTF-8 if none is configured.
     */
    private Charset getCharset() throws MojoExecutionException {
        String name = encoding == null ? "" : encoding.trim();
        if (name.isEmpty()) {
            return StandardCharsets.UTF_8;
        }

        try {
            return Charset.forName(name);
        } catch (IllegalArgumentException ex) {
            throw new MojoExecutionException(String.format("Unsupported encoding: %s", encoding), ex);
        }
    }
}
