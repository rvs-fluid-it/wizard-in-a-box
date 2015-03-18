package be.fluid_it.tools.dw.wiz2war.ci.webapp;

public class HelloWorldJarSimpleServer {
    public static void main(String[] args) throws Exception {
        HelloWorldWebApplication helloWorldWebApplication = new HelloWorldWebApplication();
        // Ensure that configuration file is loaded from the classpath
        helloWorldWebApplication.setDeployedOnContainer(true);
        helloWorldWebApplication.run(new String[]{"server", "hello-world-as-dw-jar-simple-server.yml"});
    }
}
