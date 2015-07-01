package be.fluid_it.tools.dropwizard.box.config;

import io.dropwizard.Configuration;

public interface ConfigurationWriterFactory<C extends Configuration> {
    ConfigurationWriter build(C configuration);
}
