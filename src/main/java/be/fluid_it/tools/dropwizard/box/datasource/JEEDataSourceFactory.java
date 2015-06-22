package be.fluid_it.tools.dropwizard.box.datasource;

import be.fluid_it.tools.dropwizard.box.config.JEEDataSourceConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;

import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A {@link DataSourceFactory} that provides {@link DataSource} configured in
 * JEE Container.<br />
 * <br />
 * 
 * You should override
 * {@link DatabaseConfiguration#getDataSourceFactory(io.dropwizard.Configuration)}
 * from your database bundle to return this factory, using a
 * {@link JEEDataSourceConfiguration} defined in your application
 * {@link Configuration}.
 * 
 * @see HibernateBundle#getDataSourceFactory(io.dropwizard.Configuration)
 * @see MigrationsBundle#getDataSourceFactory(io.dropwizard.Configuration)
 */
public class JEEDataSourceFactory extends DataSourceFactory {
    private JEEDataSourceConfiguration configuration;

    public JEEDataSourceFactory(JEEDataSourceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    @JsonProperty
    public Map<String, String> getProperties() {
        return configuration.getProperties();
    }

    @Override
    public ManagedDataSource build(MetricRegistry metricRegistry, String name) {
        try {
            return new JEEManagedDataSource(this.configuration.getName());
        } catch (NamingException e) {
            throw new IllegalStateException("An error has occured while opening datasource " + name, e);
        }
    }
}
