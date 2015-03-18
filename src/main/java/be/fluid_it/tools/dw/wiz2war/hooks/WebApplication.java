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
import javax.servlet.ServletException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WebApplication<C extends Configuration> extends Application<C> implements ServletContextListener {
    private static ServletContext theServletContext;
    private final List<Destroyable> destroyables = new LinkedList<Destroyable>();
    private AtomicBoolean deployedOnContainer = new AtomicBoolean(false);
    private final String[] args;

    public static ServletContext servletContext() {
        return theServletContext;
    }

    public WebApplication(String configurationFileLocation) {
        this(new String[]{"server", configurationFileLocation});
    }
    public WebApplication(String[] args) {
        this.args = args;
    }

    @Override
    public void initialize(Bootstrap<C> bootstrap) {
        if (deployedOnContainer.get()) {
            // Swap the default FileConfigurationSourceProvider
            bootstrap.setConfigurationSourceProvider(new ClasspathConfigurationSourceProvider());
        }
        super.initialize(bootstrap);
    }

    public void setDeployedOnContainer(boolean deployedOnContainer) {
        this.deployedOnContainer.getAndSet(deployedOnContainer);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        theServletContext = sce.getServletContext();
        // Probably currently too (thread-)safe
        // but hence more future proof
        if (deployedOnContainer.compareAndSet(false, true)) {
            try {
                run(args);
            } catch (Exception e) {
                throw new RuntimeException("Initialization of Dropwizard failed ...", e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        synchronized (destroyables) {
            for (Destroyable destroyable : destroyables) {
                destroyable.destroy();
            }
        }
    }

    public void registerDestroyable(Destroyable destroyable) {
        synchronized (destroyables) {
            destroyables.add(destroyable);
        }
    }

}