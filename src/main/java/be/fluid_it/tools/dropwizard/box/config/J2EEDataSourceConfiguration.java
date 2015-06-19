package be.fluid_it.tools.dropwizard.box.config;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.util.Map;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

/**
 * Embed this configuration into your dropwizard {@link Configuration} to use a
 * {@link DataSource} configured in your J2EE Container.
 */
public class J2EEDataSourceConfiguration {
    @NotNull
    private String name;

    @NotNull
    private Map<String, String> properties = Maps.newLinkedHashMap();

    /**
     * @return Properties of the DataSourceFactory (like
     *         {@link DataSourceFactory#getProperties()})
     */
    @JsonProperty
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * @return JNDI Name of the DataSource configured in J2EE Container.
     */
    @JsonProperty
    public String getName() {
        return name;
    }

}
