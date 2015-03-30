package be.fluid_it.tools.dropwizard.box.it.webapp;

import be.fluid_it.tools.dropwizard.box.WebApplication;
import be.fluid_it.tools.dropwizard.box.it.app.HelloWorldApplication;
import be.fluid_it.tools.dropwizard.box.it.app.HelloWorldConfiguration;

import javax.servlet.annotation.WebListener;

@WebListener
public class HelloWorldWebApplication extends WebApplication<HelloWorldConfiguration> {
    public HelloWorldWebApplication() {
        super(new HelloWorldApplication(), "hello-world-as-dw-war.yml");
    }
}
