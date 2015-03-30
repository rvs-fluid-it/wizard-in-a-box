package be.fluid_it.tools.dropwizard.box.it.providers;

import be.fluid_it.tools.dropwizard.box.it.support.ContextUrlProvider;

public class SampleWizSwaggerAppWarContextUrlProvider implements ContextUrlProvider {
    @Override
    public String contextUrl() {
        return "http://localhost:8887/sample-wiz-swagger-app-war";
    }
}
