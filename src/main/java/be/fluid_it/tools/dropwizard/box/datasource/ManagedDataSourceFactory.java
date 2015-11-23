package be.fluid_it.tools.dropwizard.box.datasource;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.db.ManagedDataSource;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = DefaultDataSourceFactory.class)
public interface ManagedDataSourceFactory {
  public ManagedDataSource build(MetricRegistry metricRegistry, String name);
}
