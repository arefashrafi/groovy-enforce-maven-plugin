import groovy.io.FileType
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.codehaus.groovy.control.CompilerConfiguration

@Mojo(name = "compile-static")
class CompileStatic extends AbstractMojo {

    @Parameter(name = "path", property = "path")
    private String path

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        def sourceDir = new File(path)
        def config = new CompilerConfiguration()
        if (!sourceDir.exists()) {
            return
        }
        sourceDir.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith('.groovy')) {
                def shell = new GroovyShell(config)
                def script = shell.parse(file)
                def hasCompileStatic = script.class.annotations.any { it.annotationType().simpleName == 'CompileStatic' }
                if (!hasCompileStatic) {
                    getLog().error("Class ${file.name} is missing @CompileStatic annotation")
                    throw new MojoExecutionException("Class ${file.name} is missing @CompileStatic annotation")
                }
            }
        }
    }
}
