package be.fluid_it.tools.dropwizard.box.it.rest.resources;

import be.fluid_it.tools.dropwizard.box.it.support.ContextUrlSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class SwaggerUIIT {
    @Test
    public void checkSwaggerUI() throws IOException {
        URL url = new URL(ContextUrlSupport.contextUrl() + "/api/api-docs");

        String response = IOUtils.toString(url.openStream());
        System.out.print(response);
        Assert.assertTrue(response.contains("{\"path\":\"/documented-hello-world\",\"description\":\"Hello operations\"}"));
    }

}
