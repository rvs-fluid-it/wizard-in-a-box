package be.fluid_it.tools.dropwizard.box.it.rest.resources;

import be.fluid_it.tools.dropwizard.box.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class PingAdminIT {
    @Test
    public void checkHelloWorldResource() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() +
                "/admin/ping");
        Assert.assertTrue(IOUtils.toString(url.openStream()).startsWith("pong"));
    }
}
