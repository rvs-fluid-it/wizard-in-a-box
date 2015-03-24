package be.fluid_it.tools.dw.wiz2war.ci.webapp;

import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldConfiguration;
import be.fluid_it.tools.dw.wiz2war.ci.jersey.resources.HelloWorldResource;
import be.fluid_it.tools.dw.wiz2war.hooks.WebApplication;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.annotation.WebListener;

@WebListener
public class HelloWorldWebApplication extends WebApplication<HelloWorldConfiguration> {
    public HelloWorldWebApplication() {
        super("hello-world-as-dw-war.yml");
    }

    public HelloWorldWebApplication(String configurationFileLocation) {
        super(configurationFileLocation);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
    }
}
