package be.fluid_it.tools.dropwizard.box.it.webapp;

import be.fluid_it.tools.dropwizard.box.WebApplication;
import be.fluid_it.tools.dropwizard.box.it.app.DocumentedHelloWorldApplication;
import be.fluid_it.tools.dropwizard.box.it.app.HelloWorldConfiguration;

import javax.servlet.annotation.WebListener;

@WebListener
public class HelloWorldSwaggerWebApplication extends WebApplication<HelloWorldConfiguration> {
    public HelloWorldSwaggerWebApplication() {
        super(new DocumentedHelloWorldApplication(), "hello-world-as-dw-war.yml");
    }
}
