package be.fluid_it.tools.dw.wiz2war.it.sample.servlets;


import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class HelloServletIT {
    @Test
    public void checkHelloServlet() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() +
                "/hello");
        Assert.assertEquals("Hello World from servlet.", IOUtils.toString(url.openStream()));
    }
}
