package be.fluid_it.tools.dw.wiz2war.ci.providers;

import be.fluid_it.tools.dw.wiz2war.it.support.ContextUrlProvider;

public class SampleWizAppWarContextUrlProvider implements ContextUrlProvider {
    @Override
    public String contextUrl() {
        return "http://localhost:8886/sample-wiz-app-war";
    }
}
