package be.fluid_it.tools.dw.wiz2war.hooks;

import io.dropwizard.configuration.ConfigurationSourceProvider;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathConfigurationSourceProvider implements ConfigurationSourceProvider {
    @Override
    public InputStream open(String path) throws IOException {
        return getClass().getResourceAsStream(path);
    }
}
