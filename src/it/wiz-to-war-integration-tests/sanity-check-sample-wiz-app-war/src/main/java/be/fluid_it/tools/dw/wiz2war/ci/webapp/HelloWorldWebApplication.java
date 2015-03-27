package be.fluid_it.tools.dw.wiz2war.ci.webapp;

import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldApplication;
import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldConfiguration;
import be.fluid_it.wizard.box.WebApplication;

import javax.servlet.annotation.WebListener;

@WebListener
public class HelloWorldWebApplication extends WebApplication<HelloWorldConfiguration> {
    public HelloWorldWebApplication() {
        super(new HelloWorldApplication(), "hello-world-as-dw-war.yml");
    }
}
