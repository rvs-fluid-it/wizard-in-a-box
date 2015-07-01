package be.fluid_it.tools.dropwizard.box.config;

public interface ConfigurationWriter {
    void write(String[] path, Object value) throws ConfigurationWriterException;
}
