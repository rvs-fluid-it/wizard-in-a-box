![Wizard in a box](/src/doc/wizard-in-a-box.png?raw=true) 
Wizard in a box  
===============

[![Build Status](https://api.travis-ci.org/rvs-fluid-it/wizard-in-a-box.svg)](https://travis-ci.org/rvs-fluid-it/wizard-in-a-box)

Purpose:
--------
It makes a lot of sense to deliver Dropwizard applications as self-contained, executable jars. 
But sometimes your delivery options will be severely limited by company standards. Often the JEE server on the production environment is fixed and can not be questioned. Even in this case using the Dropwizard library is still worthwhile. The biggest advantage is that it makes it easy to create a production ready service. Without Dropwizard it would be a time consuming process to harden the service.

'Wizard in a box' makes it trivial to deploy a Dropwizard application as a war on a JEE container. Also it allows you to use an embedded Jetty container during development and deploy to the targeted application server on your continous integration server. 
Hence the speed of development will be less impacted by infrastructural decisions.   

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
            <version>0.8-2-1</version>
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

Default context paths are ```/application``` for main application
and ```/admin``` for admin application. Those path can be customized in the configuration

```yaml
server:
  type: bridge
  applicationContextPath: /api/v1
  adminContextPath: /admin
```

Assets (see http://www.dropwizard.io/manual/core.html#serving-assets) can be served from the root context of the web application.

```yaml
server:
  type: bridge
  applicationContextPath: /api/v1
  adminContextPath: /admin
  servletsMappedFromRootContext:
    - assets  
```
The property ```servletsMappedFromRootContext``` is the list of application servlets which should be directly coupled to the webapp root context. Hence they will not be prefixed with the application context path. 


If needed add application server specific files to the  src/main/webapp folder (Tomcat -> META-INF/context.xml, Weblogic -> WEB-INF/weblogic.xml, ...)

Sample:
-------
See src/it/wizard-in-a-box-integration-tests/sanity-check-sample-wiz-app-war (The integration test can serve as a sample as well)

Under the hood:
---------------

![Technical design](/src/doc/wizard-in-a-box-design.png?raw=true)

When you  wrap your regular Dropwizard application with a WebApplication, the WebApplication will swap Dropwizards default ConfigurationSourceProvider with an implementation reading the configuration file from the classpath. The WebApplication implements a JEE WebListener, hence the JEE container will boot the Dropwizard application when the war is started on the container. DropWizard will then parse the configuration. By defining the servertype as a bridge in the configuration file. Dropwizard will instantiate a JEEBridgeFactory instead of a DefaultServerFactory or a SimpleServerFactory. The JEEBridgeFactory will dynamically register servlets, filters, attributes at the JEE servlet context. Also it will create a fake  Jetty server instead of a real one. The fake server has the start and stop of the underlying Jetty server disabled.
