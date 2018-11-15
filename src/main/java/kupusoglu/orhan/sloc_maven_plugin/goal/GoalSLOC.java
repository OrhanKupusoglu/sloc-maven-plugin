package kupusoglu.orhan.sloc_maven_plugin.goal;

import kupusoglu.orhan.sloc_maven_plugin.engine.Common;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;


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
    defaultPhase = LifecyclePhase.VALIDATE,
    requiresDependencyResolution = ResolutionScope.TEST
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
     * trim package names
     * @parameter
     */
    @Parameter(property = "trimPkgNames", defaultValue = "true")
    private boolean trimPkgNames;

    public void execute() throws MojoExecutionException {
        try {
            Common.countLines(getLog(),
                              project.getBasedir().getAbsolutePath(),
                              srcMain,
                              fileExt,
                              trimPkgNames);
        } catch (MojoExecutionException e) {
            getLog().error(e.getMessage());
        }
    }
}
