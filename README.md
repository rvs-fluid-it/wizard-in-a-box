Wizard in a box
===============

Purpose:
--------
It makes a lot of sense to deliver Dropwizard applications as self-contained, executable jars. 
But sometimes your delivery options will be severely limited by company standards. Often the JEE server on the production environment is fixed and can not be questioned. Even in this case using the Dropwizard library is still really worthwhile. The biggest advantage is that it makes it easy to create a production ready service. Without Dropwizard it would be a time consuming process to harden the service.
'Wizard in a box' makes it trivial to deploy a Dropwizard application as a war on a JEE container. Also it allows you to use an embedded Jetty container during development and deploy to the targeted application server on the continous integration server. Hence the speed of development will be less impacted by infrastructural decisions.   

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

When you  wrap your regular Dropwizard application with a WebApplication,  the WebApplication will swap Dropwizards default ConfigurationSourceProvider with an implementation reading the configuration file from the classpath. Since a WebApplication is a JEE WebListener, the JEE container will boot the Dropwizard application when the war is started on the container. DropWizard will parse the configuration. In the configuration file is the servertype defined as bridge. Hence a JEEBridgeFactory will be created by Dropwizard instead of a DefaultServerFactory or a SimpleServerFactory. The JEEBridgeFactory will dynamically register servlets, filters, attributes at the JEE servlet context. Also it will create a fake  Jetty server instead of a real one. The fake server has the start and stop of the underlying Jetty server disabled.
