package be.fluid_it.tools.dw.wiz2war.bridge;

import io.dropwizard.server.ServerFactory;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Server;

public class ServerBridgeFactory implements ServerFactory {
    @Override
    public Server build(Environment environment) {
        return new ServerAdapter(environment);
    }
}
