package be.fluid_it.tools.dw.wiz2war.hooks;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.LinkedList;
import java.util.List;

public abstract class WebApplication<C extends Configuration> extends Application<C> implements ServletContextListener {
    private static ServletContext theServletContext;
    private final List<Destroyable> destroyables = new LinkedList<Destroyable>();
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
        // Swap the default FileConfigurationSourceProvider
        bootstrap.setConfigurationSourceProvider(new ClasspathConfigurationSourceProvider());
        super.initialize(bootstrap);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        theServletContext = sce.getServletContext();
        try {
            run(args);
        } catch (Exception e) {
            throw new RuntimeException("Initialization of Dropwizard failed ...", e);
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