package be.fluid_it.tools.dropwizard.box.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.JsonConfigurationFactory;

import javax.validation.Validator;
import java.io.File;
import java.io.IOException;

public class BridgedConfigurationFactory<T extends Configuration> extends JsonConfigurationFactory<T> {
    private ConfigurationBridge<T> configurationBridge;

    public BridgedConfigurationFactory(ConfigurationBridge bridge, Class<T> klass, Validator validator, ObjectMapper objectMapper, String propertyPrefix) {
        super(klass, validator, objectMapper, propertyPrefix);
        configurationBridge = bridge;
    }

    @Override
    public T build(File file) throws IOException, ConfigurationException {
        T configuration = super.build(file);
        configurationBridge.load(configuration);
        return configuration;
    }

    @Override
    public T build(ConfigurationSourceProvider provider, String path) throws IOException, ConfigurationException {
        T configuration = super.build(provider, path);
        configurationBridge.load(configuration);
        return configuration;
    }

    @Override
    public T build() throws IOException, ConfigurationException {
        T configuration = super.build();
        configurationBridge.load(configuration);
        return configuration;
    }
}
