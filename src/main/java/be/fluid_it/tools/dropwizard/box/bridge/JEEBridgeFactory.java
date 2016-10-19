package be.fluid_it.tools.dropwizard.box.bridge;

import be.fluid_it.tools.dropwizard.box.WebApplication;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jetty9.InstrumentedHandler;
import com.codahale.metrics.jetty9.InstrumentedQueuedThreadPool;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.server.ServerFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.validation.constraints.NotNull;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@JsonTypeName("bridge")
public class JEEBridgeFactory extends SimpleServerFactory implements ServerFactory {
  private Logger logger = LoggerFactory.getLogger(JEEBridgeFactory.class);

  @NotNull
  private String[] servletsMappedFromRootContext = new String[]{};

  @JsonProperty
  public String[] getServletsMappedFromRootContext() {
    return servletsMappedFromRootContext;
  }

  @JsonProperty
  public void setServletsMappedFromRootContext(String[] servletsMappedFromRootContext) {
    this.servletsMappedFromRootContext = servletsMappedFromRootContext;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  @Override
  public Server build(Environment environment) {
    JEEBridge server = new JEEBridge(environment, createThreadPool(environment.metrics()));

    environment.getAdminContext().setContextPath(getAdminContextPath());
    final Handler adminHandler = createAdminServlet(server,
        environment.getAdminContext(),
        environment.metrics(),
        environment.healthChecks());
    registerOnJEEServletContext(getAdminContextPath(), adminHandler);
    environment.getApplicationContext().setContextPath(getApplicationContextPath());
    final Handler applicationHandler = createAppServlet(server,
        environment.jersey(),
        environment.getObjectMapper(),
        environment.getValidator(),
        environment.getApplicationContext(),
        environment.getJerseyServletContainer(),
        environment.metrics());
    registerOnJEEServletContext(getApplicationContextPath(), applicationHandler);
    return server;
  }

  protected ThreadPool createThreadPool(MetricRegistry metricRegistry) {
    // Keep the number of threads low. The embedded jetty server should not do any work.
    // All work is done by the JEE container
    int minThreads = 1;
    int maxThreads = 4;
    final BlockingQueue<Runnable> queue = new BlockingArrayQueue<>(minThreads, maxThreads, getMaxQueuedRequests());
    final InstrumentedQueuedThreadPool threadPool =
        new InstrumentedQueuedThreadPool(metricRegistry, maxThreads, minThreads,
            (int) getIdleThreadTimeout().toMilliseconds(), queue);
    threadPool.setName("dw-in-a-box");
    return threadPool;
  }

  private void registerOnJEEServletContext(String contextPath, Handler jettyHandler) {
    Handler handler;
    if (jettyHandler instanceof InstrumentedHandler) {
      handler = ((InstrumentedHandler) jettyHandler).getHandler();
    } else {
      handler = jettyHandler;
    }
    if (handler instanceof ServletContextHandler) {
      ServletContextHandler servletContextHandler = (ServletContextHandler) handler;
      Map<String, String> servletContextInitParameters = servletContextHandler.getInitParams();
      for (Map.Entry<String, String> entry : servletContextInitParameters.entrySet()) {
        logger.info("ServletContext init parameter [" + entry.getKey() + "->" + entry.getValue() + "] detected ...");
        WebApplication.servletContext().setInitParameter(entry.getKey(), entry.getValue());
      }
      ServletMapping[] servletMappings = servletContextHandler.getServletHandler().getServletMappings();
      for (ServletMapping servletMapping : servletMappings) {
        String servletName = servletMapping.getServletName();
        String[] servletPathSpecs = servletMapping.getPathSpecs();
        logger.info("Servlet mapping [" + servletName + "->" + print(isContextPrefixed(servletName) ? prefixedPathSpecs(contextPath, servletPathSpecs) : servletPathSpecs) + "] detected ...");
      }
      ServletHolder[] servlets = servletContextHandler.getServletHandler().getServlets();
      for (ServletHolder servletHolder : servlets) {
        String servletName = servletHolder.getName();
        logger.info("Servlet [" + servletName + "] detected ...");
        try {
          ServletRegistration.Dynamic servletRegistration = WebApplication.servletContext().addServlet(servletName, servletHolder.getServlet());
          for (Map.Entry<String, String> entry : servletHolder.getInitParameters().entrySet()) {
            logger.info("Servlet init parameter [" + entry.getKey() + "->" + entry.getValue() + "] detected ...");
            servletRegistration.setInitParameter(entry.getKey(), entry.getValue());
          }
          if (servletHolder.isAsyncSupported()) {
            servletRegistration.setAsyncSupported(true);
          }
          for (ServletMapping servletMapping : servletMappings) {
            if (servletName.equals(servletMapping.getServletName())) {
              String[] servletPathSpecs = servletMapping.getPathSpecs();
              servletRegistration.addMapping(isContextPrefixed(servletName) ? prefixedPathSpecs(contextPath, servletPathSpecs) : servletPathSpecs);
              break;
            }
          }
        } catch (ServletException e) {
          throw new RuntimeException(e);
        }
      }
      FilterMapping[] filterMappings = servletContextHandler.getServletHandler().getFilterMappings();
      for (FilterMapping filterMapping : filterMappings) {
        logger.info("Filter mapping [" + filterMapping.getFilterName() + "->" + print(prefixedPathSpecs(contextPath, filterMapping.getPathSpecs())) + "] detected ...");
      }
      FilterHolder[] filters = servletContextHandler.getServletHandler().getFilters();
      for (FilterHolder filterHolder : filters) {
        String filterName = filterHolder.getName();
        logger.info("Filter [" + filterName + "] detected ...");
        FilterRegistration.Dynamic filterRegistration = null;
        if (filterHolder.getFilter() != null) {
          filterRegistration = WebApplication.servletContext().addFilter(filterName, filterHolder.getFilter());
        } else {
          filterRegistration = WebApplication.servletContext().addFilter(filterName, filterHolder.getHeldClass());
        }
        if (filterHolder.isAsyncSupported()) {
          filterRegistration.setAsyncSupported(true);
        }

        for (Map.Entry<String, String> entry : filterHolder.getInitParameters().entrySet()) {
          logger.info("Filter init parameter [" + entry.getKey() + "->" + entry.getValue() + "] detected ...");
          filterRegistration.setInitParameter(entry.getKey(), entry.getValue());
        }
        for (FilterMapping filterMapping : filterMappings) {
          if (filterName.equals(filterMapping.getFilterName())) {
            // TODO null -> DispatcherType.REQUEST
            // also support none default settings
            filterRegistration.addMappingForUrlPatterns(null, true,
                prefixedPathSpecs(contextPath, filterMapping.getPathSpecs()));
            break;
          }
        }
        for (EventListener eventListener : servletContextHandler.getEventListeners()) {
          logger.info("EventListener on [" + eventListener.getClass().getName() + "] detected ...");
          WebApplication.servletContext().addListener(eventListener);
        }
        ;
      }
      ContextHandler.Context dropWizardServletContext = servletContextHandler.getServletContext();
      Enumeration<String> names = dropWizardServletContext.getAttributeNames();
      while (names.hasMoreElements()) {
        String name = names.nextElement();
        logger.info("Attribute [" + name + "->" + dropWizardServletContext.getAttribute(name) + "] detected ...");
        WebApplication.servletContext().setAttribute(name, dropWizardServletContext.getAttribute(name));
      }
    }
  }

  private boolean isContextPrefixed(String servletName) {
    if (servletName != null) {
      for (String rootContextServletName : servletsMappedFromRootContext) {
        if (servletName.equals(rootContextServletName)) return false;
      }
    }
    return true;
  }

  private String[] prefixedPathSpecs(String contextPath, String[] pathSpecs) {
    if (pathSpecs == null) return new String[]{contextPath};
    String[] prefixedPathSpecs = new String[pathSpecs.length];
    for (int i = 0; i < pathSpecs.length; i++) {
      prefixedPathSpecs[i] = contextPath + pathSpecs[i];
    }
    return prefixedPathSpecs;
  }

  private String print(String[] pathSpecs) {
    StringBuffer buffer = new StringBuffer("[");
    boolean first = true;
    for (String pathSpec : pathSpecs) {
      if (first) {
        first = false;
      } else {
        buffer.append(", ");
      }
      buffer.append(pathSpec);
    }
    buffer.append("]");
    return buffer.toString();
  }
}
