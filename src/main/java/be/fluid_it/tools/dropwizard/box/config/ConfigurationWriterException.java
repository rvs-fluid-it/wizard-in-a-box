package be.fluid_it.tools.dropwizard.box.config;

public class ConfigurationWriterException extends Exception {
    public ConfigurationWriterException() {
    }

    public ConfigurationWriterException(String message) {
        super(message);
    }

    public ConfigurationWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationWriterException(Throwable cause) {
        super(cause);
    }

    public ConfigurationWriterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
