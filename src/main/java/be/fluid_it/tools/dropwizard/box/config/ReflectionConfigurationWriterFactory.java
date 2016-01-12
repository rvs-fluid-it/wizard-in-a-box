package be.fluid_it.tools.dropwizard.box.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.Configuration;

public class ReflectionConfigurationWriterFactory<C extends Configuration> implements ConfigurationWriterFactory<C> {

	private ObjectMapper mapper ;

	public ReflectionConfigurationWriterFactory() {
		this.mapper = new ObjectMapper(); 
	}

	public ReflectionConfigurationWriterFactory(ObjectMapper mapper) {
		this.mapper = mapper ;
	}
	
	@Override
    public ConfigurationWriter build(C configuration) {
        return new ReflectionConfigurationWriter(configuration, mapper);
    }
}
