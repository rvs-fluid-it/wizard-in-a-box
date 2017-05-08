package be.fluid_it.tools.dropwizard.box.it.providers;

import be.fluid_it.tools.dropwizard.box.it.support.ContextUrlProvider;

public class SampleWizAppWarContextUrlProvider implements ContextUrlProvider {

    public String contextUrl() {
        return "http://localhost:8886/sample-wiz-app-war";
    }
}
