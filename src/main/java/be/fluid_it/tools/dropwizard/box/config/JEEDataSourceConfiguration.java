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
public class JEEDataSourceConfiguration {
    // The default key matches Tomcat
    // (Dependending on the target application server used you should eventually override the default)
    // See https://tomcat.apache.org/tomcat-8.0-doc/jndi-resources-howto.html
    @NotNull
    private String datasourcesJndKey = "java:comp/env";

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

    /**
     * @return JNDI key under which the datasources are registered.
     */
    @JsonProperty
    public String getDatasourcesJndiKey() {
        return this.datasourcesJndKey;
    }
}
