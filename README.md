# ENVM
_Envm_ is an environment manager based on maven for Java projects. This is a fork of the woko [maven plugin](https://github.com/pojosontheweb/woko/tree/develop/wmaven) 
where we've just remove the dependencies on [Woko framework](http://www.pojosontheweb.com/).

## Installation
_Envm_ is a maven plugin, your pom.xml should look like :

```
<build>
    <finalName>MyApp</finalName>
    <plugins>
        <plugin>
            <groupId>org.abnt</groupId>
            <artifactId>envm</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                    <id>envm.environment</id>
                    <phase>process-resources</phase>
                    <goals>
                        <goal>env</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## How it works
_Envm_ allow to have different resources depending on the context (e.g. production, test, dev, etc.). They are plain folders under the project root :

```
* MyApp/
    environments/
        dev/
            log4j.properties
            hibernate.cfg.xml
        prod/
            log4j.properties
            hibernate.cfg.xml
```

You can switch from various environments easily by using :
```
$> mvn clean install -Denvm.env=dev
```

The plugin will recursively copy (and thereby possibly overwrite existing resources) the files found in the environment folder 
```<project_root>/environments/myenv``` to the ```target/classes``` folder of your project.

