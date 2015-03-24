package test;

import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldApplication;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;


public class SampleWizAppTest {
    Logger logger = LoggerFactory.getLogger("TEST");

    @Test
    @Ignore
    public void checkSampleWizApp() throws Exception {
        String current = new java.io.File( "." ).getCanonicalPath();
        logger.info("Current dir:"+current);
        HelloWorldApplication.main(new String[] {"server", "./src/it/wiz-to-war-integration-tests/sample-wiz-app/target/test-classes/hello-world-as-dw-jar.yml"});
        URL url = new URL("http://localhost:8880/hello-world?name=Merlin");
        Assert.assertEquals("{\"id\":1,\"content\":\"Hello, Merlin!\"}", IOUtils.toString(url.openStream()));
        URL pingUrl = new URL("http://localhost:8881/ping");
        Assert.assertTrue(IOUtils.toString(pingUrl.openStream()).startsWith("pong"));
    }

}
