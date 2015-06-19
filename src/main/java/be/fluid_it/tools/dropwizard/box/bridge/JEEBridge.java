package be.fluid_it.tools.dropwizard.box.bridge;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JEEBridge extends Server {
    private Logger logger = LoggerFactory.getLogger(JEEBridge.class);

    private final Connector[] connectors = new Connector[0];
    private final Handler[] handlers = new Handler[0];

    public JEEBridge(ThreadPool threadPool) {
        super(threadPool);
    }

    @Override
    public Connector[] getConnectors() {
        return connectors;
    }

    @Override
    public void addConnector(Connector connector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeConnector(Connector connector) {
    }

    @Override
    public void handle(HttpChannel<?> connection) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleAsync(HttpChannel<?> connection) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Handler getHandler() {
        return null;
    }

    @Override
    public Handler[] getHandlers() {
        return handlers;
    }

    @Override
    public void setHandler(Handler handler) {
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getStopTimeout() {
        return 0;
    }

    @Override
    public boolean isDumpAfterStart() {
        return false;
    }

    @Override
    public boolean isDumpBeforeStop() {
        return false;
    }

    @Override
    protected void doStart() throws Exception {
        logger.info("Start Bridged Jetty server ...");
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        logger.info("Stop Bridged Jetty server ...");
        super.doStop();
    }

}
