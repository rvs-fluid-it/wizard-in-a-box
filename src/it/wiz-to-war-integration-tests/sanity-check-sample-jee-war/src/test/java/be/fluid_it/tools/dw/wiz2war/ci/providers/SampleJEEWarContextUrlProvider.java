package be.fluid_it.tools.dw.wiz2war.ci.providers;

import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlProvider;

public class SampleJEEWarContextUrlProvider implements ContextUrlProvider {
    @Override
    public String contextUrl() {
        return "http://localhost:8885/sample-jee-war";
    }
}
