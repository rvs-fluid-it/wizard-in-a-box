import be.fluid_it.tools.dw.wiz2war.ci.app.DocumentedHelloWorldApplication;
import be.fluid_it.tools.dw.wiz2war.it.support.WizRunner;

public class SampleWizSwaggerAppRunner {
    public static void main(String[] args) throws Exception {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.print("Current dir:" + current);

        new WizRunner(new DocumentedHelloWorldApplication(), "./target/classes/hello-world-as-dw-jar.yml").run();
    }
}