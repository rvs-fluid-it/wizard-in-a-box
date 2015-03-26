package be.fluid_it.tools.dw.wiz2war.ci.webapp.resources;

import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class SwaggerUIIT {
    @Test
    public void checkSwaggerUI() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() + "/application/api-docs");

        String response = IOUtils.toString(url.openStream());
        System.out.print(response);
        Assert.assertTrue(response.contains("{\"path\":\"/documented-hello-world\",\"description\":\"Hello operations\"}"));
    }

}
