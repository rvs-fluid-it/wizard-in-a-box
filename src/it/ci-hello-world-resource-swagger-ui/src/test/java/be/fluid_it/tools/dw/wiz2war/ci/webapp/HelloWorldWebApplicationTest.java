package be.fluid_it.tools.dw.wiz2war.ci.webapp;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.Assert;


import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HelloWorldWebApplicationTest {
    @Test
    public void theHelloWorldWebApplicationCanBeRunAsAnExecutableJar() throws Exception {
        HelloWorldWebApplication helloWorldWebApplication = new HelloWorldWebApplication();
        // Ensure that configuration file is loaded from the classpath
        helloWorldWebApplication.setDeployedOnContainer(true);
        //helloWorldWebApplication.run(new String[]{"server", "hello-world-as-dw-jar.yml"});
        //URL url = new URL("http://localhost:8880/hello-world");
        //Assert.assertEquals("{\"id\":1,\"content\":\"Hello, Stranger!\"}", IOUtils.toString(url.openStream()));
    }
}
