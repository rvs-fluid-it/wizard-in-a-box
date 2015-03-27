package be.fluid_it.tools.dw.wiz2war.ci.webapp;

import be.fluid_it.tools.dw.wiz2war.ci.app.DocumentedHelloWorldApplication;
import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldConfiguration;
import be.fluid_it.wizard.box.WebApplication;

import javax.servlet.annotation.WebListener;

@WebListener
public class HelloWorldSwaggerWebApplication extends WebApplication<HelloWorldConfiguration> {
    public HelloWorldSwaggerWebApplication() {
        super(new DocumentedHelloWorldApplication(), "hello-world-as-dw-war.yml");
    }
}
