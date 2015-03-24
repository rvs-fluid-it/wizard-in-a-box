package test.be.fluid_it.tools.dw.wiz2war.it.sample.wiz.app.resources;

import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class HelloWorldResourceIT {
    @Test
    public void checkHelloWorldResource() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() +
                "/application/hello-world?name=Merlin");
        Assert.assertEquals("{\"id\":1,\"content\":\"Hello, Merlin!\"}", IOUtils.toString(url.openStream()));
    }

}
