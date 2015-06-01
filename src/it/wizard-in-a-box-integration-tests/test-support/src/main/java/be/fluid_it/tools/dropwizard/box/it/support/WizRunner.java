package be.fluid_it.tools.dropwizard.box.it.support;

import io.dropwizard.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WizRunner {
    private final Application application;
    private final String relativePathYmlConfig;
    private final Logger logger = LoggerFactory.getLogger("TEST");

    private WizRunner(Application application, String relativePathYmlConfig) {
        this.application = application;
        this.relativePathYmlConfig = relativePathYmlConfig;
    }

    public WizRunner(Application application, String moduleName, String configFileName) {
        this(application, relativePath(moduleName, configFileName));
    }

    public void run() throws Exception {
        String current = new java.io.File( "." ).getCanonicalPath();
        logger.info("Current dir:" + current);
        application.run(new String[] { "server", relativePathYmlConfig });
    }


    public static String relativePath(String module, String configFile) {
        StringBuffer pathBuffer = new StringBuffer("./");
        if (isRunningFromIDE()) {
            pathBuffer.append("src/it/wizard-in-a-box-integration-tests/");
            pathBuffer.append(module);
            pathBuffer.append("/src/main/resources/");
        } else {
            pathBuffer.append("target/classes/");
        }
        pathBuffer.append(configFile);
        return pathBuffer.toString();
    }

    public static boolean isRunningFromIDE() {
        String workingDir = System.getProperty("user.dir");
        boolean runningFromIDE = workingDir.endsWith("wizard-in-a-box");
        if (runningFromIDE) {
            System.out.println("Current working directory : " + workingDir);
        }
        return runningFromIDE;
    }
}
