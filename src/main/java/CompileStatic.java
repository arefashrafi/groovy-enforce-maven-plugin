import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Mojo(name = "compile-static")
public class CompileStatic extends AbstractMojo {

    @Parameter(name = "path", property = "path")
    private String path;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File sourceDir = new File(path);

        if (!sourceDir.exists()) {
            return;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            List<Path> groovyFiles = paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".groovy"))
                    .toList();

            for (Path filePath : groovyFiles) {
                File file = filePath.toFile();
                List<String> lines = Files.readAllLines(file.toPath());

                boolean hasCompileStatic = lines.stream()
                        .anyMatch(line -> line.contains("@CompileStatic"));

                if (!hasCompileStatic) {
                    getLog().error("Class " + file.getName() + " is missing @CompileStatic annotation");
                    throw new MojoExecutionException("Class " + file.getName() + " is missing @CompileStatic annotation");
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading files", e);
        }
    }
}