package be.fluid_it.tools.dropwizard.box.config;

import io.dropwizard.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import java.util.ArrayList;
import java.util.List;

public class JndiConfigurationBridge<C extends Configuration> implements ConfigurationBridge<C> {
    private Logger log = LoggerFactory.getLogger(JndiConfigurationBridge.class);

    private final ConfigurationWriterFactory writerFactory;
    private final String configurationContext;

    private static final String DEFAULT_CONTEXT = "java:comp/env/dropwizard/configuration";

    public JndiConfigurationBridge() {
        this(DEFAULT_CONTEXT);
    }

    public JndiConfigurationBridge(String configurationContext) {
        this(new ReflectionConfigurationWriterFactory(), configurationContext);
    }

    public JndiConfigurationBridge(ConfigurationWriterFactory writerFactory) {
        this(writerFactory, DEFAULT_CONTEXT);
    }

    public JndiConfigurationBridge(ConfigurationWriterFactory writerFactory, String configurationContext) {
        this.configurationContext = configurationContext;
        this.writerFactory = writerFactory;
    }

    public void load(C configuration) {
        ConfigurationWriter configurationWriter = writerFactory.build(configuration);

        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup(configurationContext);
            writeEnumeration(envCtx, configurationWriter, null);

        } catch (NamingException e) {
            log.error("Can't configure property from J2EE Context", e);
        }

    }

    private void writeEnumeration(Context parentContext, ConfigurationWriter configurationWriter, List<String> path) {
        try {
            NamingEnumeration<NameClassPair> list = parentContext.list("");
            while (list.hasMore()) {
                NameClassPair next = list.next();
                String name = next.getName();
                if (path == null) {
                    path = new ArrayList<>();
                }
                path.add(name);
                try {
                    Object value = parentContext.lookup(name);
                    if (value instanceof Context) {
                        writeEnumeration((Context) value, configurationWriter, path);
                    } else {
                        configurationWriter.write(path.toArray(new String[path.size()]), value);
                    }
                } catch (NamingException e) {
                    log.error("Can't read property " + name + " from JNDI Naming Context " + configurationContext, e);
                } catch (ConfigurationWriterException e) {
                    log.error("Can't write property " + name + " to Dropwizard Configuration", e);
                }
                path.remove(path.size() - 1);

            }
        } catch (NamingException e) {
            log.error("Can't configure property from J2EE Context", e);
        }


    }
}
