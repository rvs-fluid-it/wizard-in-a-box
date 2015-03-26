package be.fluid_it.tools.dw.wiz2war.it.support;

import io.dropwizard.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WizRunner {
    private final Application application;
    private final String relativePathYmlConfig;
    private final Logger logger = LoggerFactory.getLogger("TEST");

    public WizRunner(Application application, String relativePathYmlConfig) {
        this.application = application;
        this.relativePathYmlConfig = relativePathYmlConfig;
    }

    public void run() throws Exception {
        String current = new java.io.File( "." ).getCanonicalPath();
        logger.info("Current dir:" + current);
        application.run(new String[] { "server", relativePathYmlConfig });
    }
}
