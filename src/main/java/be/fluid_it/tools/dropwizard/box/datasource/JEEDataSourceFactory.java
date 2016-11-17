package be.fluid_it.tools.dropwizard.box.datasource;

import be.fluid_it.tools.dropwizard.box.config.JEEDataSourceConfiguration;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.ManagedDataSource;

import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A {@link DataSourceFactory} that provides {@link DataSource} configured in
 * JEE Container.
 * 
 * You should override
 * {@link DatabaseConfiguration#getDataSourceFactory(io.dropwizard.Configuration)}
 * from your database bundle to return this factory, using a
 * {@link JEEDataSourceConfiguration} defined in your application
 * {@link Configuration}.
 */
@JsonTypeName("jee")
public class JEEDataSourceFactory extends JEEDataSourceConfiguration implements ManagedDataSourceFactory {
    @Override
    public ManagedDataSource build(MetricRegistry metricRegistry, String name) {
        try {
            return new JEEManagedDataSource(isResourceRef() ? getDatasourcesJndiKey(): null, getName());
        } catch (NamingException e) {
            throw new IllegalStateException("An error has occured while opening datasource " + name, e);
        }
    }
}
