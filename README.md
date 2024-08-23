# Build

```
mvn clean install -U
```

```xml

<plugin>
    <groupId>io.ashraare</groupId>
    <artifactId>groovy-enforce-maven-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>compile-static</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <path>${project.basedir}/src/main</path>
    </configuration>
</plugin>
```
