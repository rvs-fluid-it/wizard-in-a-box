package be.fluid_it.tools.dw.wiz2war.hooks;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.CheckCommand;
import io.dropwizard.cli.Cli;
import io.dropwizard.cli.ServerCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.util.JarLocation;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class ApplicationHook<C extends Configuration> extends Application<C> implements ServletContextListener {

    private ServletContext theServletContext;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.theServletContext = sce.getServletContext();
        final Bootstrap<C> bootstrap = new Bootstrap<>(this);
        // Swap FileConfigurationSourceProvider with
        bootstrap.setConfigurationSourceProvider(new ClasspathConfigurationSourceProvider());
        bootstrap.addCommand(new ServerCommand<>(this));
        bootstrap.addCommand(new CheckCommand<>(this));
        initialize(bootstrap);
        final Cli cli = new Cli(new JarLocation(getClass()), bootstrap, System.out, System.err);
        try {
            if (!cli.run(new String[] {"server", ".yml"})) {
               throw new RuntimeException("cli.run did not succeed ...");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while executing cli.run : " + e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


}
