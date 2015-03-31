package be.fluid_it.tools.dropwizard.box.it.providers;

import be.fluid_it.tools.dropwizard.box.it.support.ContextUrlProvider;

public class SampleJEEWarContextUrlProvider implements ContextUrlProvider {
    @Override
    public String contextUrl() {
        return "http://localhost:8885/sample-jee-war";
    }
}
