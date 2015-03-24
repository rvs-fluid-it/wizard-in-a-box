package test;

import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldApplication;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;

public class SampleWizAppSimpleServerTest {


    @Test
    @Ignore
    public void checkSampleWizAppSimpleServer() throws Exception {
        HelloWorldApplication.main(new String[]{"server", "./src/it/wiz-to-war-integration-tests/sample-wiz-app/target/test-classes/hello-world-as-dw-jar-simple-server.yml"});
        URL url = new URL("http://localhost:8880/application/hello-world?name=Merlin");
        Assert.assertEquals("{\"id\":1,\"content\":\"Hello, Merlin!\"}", IOUtils.toString(url.openStream()));
        URL pingUrl = new URL("http://localhost:8880/admin/ping");
        Assert.assertTrue(IOUtils.toString(pingUrl.openStream()).startsWith("pong"));
    }

}
