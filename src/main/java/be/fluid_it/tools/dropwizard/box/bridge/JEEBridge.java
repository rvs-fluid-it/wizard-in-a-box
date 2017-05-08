package be.fluid_it.tools.dropwizard.box.bridge;

import be.fluid_it.tools.dropwizard.box.WebApplication;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JEEBridge extends Server {
    private Logger logger = LoggerFactory.getLogger(JEEBridge.class);

    private final Environment environment;
    private ThreadPool threadPool;

    private final List<Connector> connectors = new LinkedList<Connector>();
    private final Map<String, Object> attributesByName = new HashMap<String, Object>();
    private final List<Handler> handlers = new LinkedList<Handler>();

    private enum ServerState {
        FAILED, STARTING, STARTED, STOPPING, STOPPED;
    }

    private ServerState serverState = ServerState.STARTED;

    public JEEBridge(Environment environment, ThreadPool threadPool) {
      super(threadPool);
      this.environment = environment;
    }

    @Override
    public Connector[] getConnectors() {
        synchronized (connectors) {
            return connectors.toArray(new Connector[connectors.size()]);
        }
    }

    @Override
    public void addConnector(Connector connector) {
        synchronized (connectors) {
            connectors.add(connector);
        }
    }

    @Override
    public void removeConnector(Connector connector) {
        connectors.remove(connector);
    }


    @Override
    public void clearAttributes() {
        synchronized (attributesByName) {
            attributesByName.clear();
        }
    }

    @Override
    public Object getAttribute(String name) {
        synchronized (attributesByName) {
            return attributesByName.get(name);
        }
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        synchronized (attributesByName) {
            return new Vector(attributesByName.keySet()).elements();
        }
    }

    @Override
    public void removeAttribute(String name) {
        synchronized (attributesByName) {
            attributesByName.remove(name);
        }
        WebApplication.servletContext().removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object attribute) {
        synchronized (attributesByName) {
            attributesByName.put(name, attribute);
        }
        WebApplication.servletContext().setAttribute(name, attribute);
    }

    @Override
    public Handler getHandler() {
        // TODO
        return null;
    }

    @Override
    public Handler[] getHandlers() {
        if (handlers == null) {
            return new Handler[0];
        }
        synchronized (handlers) {
            return handlers.toArray(new Handler[handlers.size()]);
        }
    }

    @Override
    public void setHandler(Handler handler) {
        logger.info("Set handler ", handler.getClass().getName());
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    // Server Lifecycle

    @Override
    public boolean isRunning() {
        return STARTED.equals(serverState);
    }

    @Override
    public boolean isStarted() {
        return STARTED.equals(serverState);
    }

    @Override
    public boolean isStarting() {
        return STARTING.equals(serverState);
    }

    @Override
    public boolean isStopping() {
        return STOPPING.equals(serverState);
    }

    @Override
    public boolean isStopped() {
        return STOPPED.equals(serverState);
    }

    @Override
    public boolean isFailed() {
        return FAILED.equals(serverState);
    }

    private final List<LifeCycle.Listener> lifeCycleListeners = new LinkedList<LifeCycle.Listener>();

    @Override
    public void addLifeCycleListener(LifeCycle.Listener listener) {
        synchronized (lifeCycleListeners) {
            lifeCycleListeners.add(listener);
        }
    }

    @Override
    public void removeLifeCycleListener(LifeCycle.Listener listener) {
        synchronized (lifeCycleListeners) {
            lifeCycleListeners.remove(listener);
        }
    }

    @Override
    public String getState() {
        return serverState != null ? serverState.name() : null;
    }

    @Override
    public long getStopTimeout() {
        return 0;
    }


    // Start

    @Override
    protected void doStart() throws Exception {
        logger.info("Dummy start Jetty server ...");
    }

    @Override
    protected void doStop() throws Exception {
        logger.info("Dummy stop Jetty server ...");
    }

}
