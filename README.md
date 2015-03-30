Wizard in a box
===============

Purpose:
--------
Deploy a Dropwizard application on a JEE application server. 

Usage:
------

Add an extra Maven module to your modules pom and create a pom for your new war module:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
    ...

    <artifactId>...-war</artifactId>
    <packaging>war</packaging>

    <dependencies>
        <!-- Your module containing the Dropwizard application -->
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>...-rest</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>be.fluid-it.tools.dropwizard</groupId>
            <artifactId>wizard-in-a-box</artifactId>
            <version>0.8-1-1</version>
        </dependency>
        <!-- Logging implementation that you want to use underneath Slf4j --> 
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>...</version>
        </dependency>
        <!-- Needed or not needed depending on the version delivered by your targeted application server -->
        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
            <version>1.1.0.Final</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>users-service</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>...</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
            ...
        </plugins>
    </build>
</project>
```

Wrap your Dropwizard application in a WebApplication:

```java
package ...

import ...

@WebListener
public class YourWebApplication  extends WebApplication<YourDropwizardConfiguration> {
    public YourWebApplication() {
        super(new YourDropwizardApplication(), "your-dropwizard-war-config.yml");
    }
}
```

Add the configuration file to the src/main/resources folder of the war module:
```yaml
...

server:
  type: bridge

...
```

If needed add application server specific files to the  src/main/webapp folder (Tomcat -> META-INF/context.xml, Weblogic -> WEB-INF/weblogic.xml, ...)

Under the hood:
---------------
