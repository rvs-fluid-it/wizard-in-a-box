package be.fluid_it.tools.dw.wiz2war.ci.webapp.resources;

import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class DocumentedHelloWorldResourceIT {
    @Test
    public void checkHelloWorldResource() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() +
                "/application/documented-hello-world?name=Merlin");
        Assert.assertEquals("{\"id\":1,\"content\":\"Hello, Merlin!\"}", IOUtils.toString(url.openStream()));
    }

}
