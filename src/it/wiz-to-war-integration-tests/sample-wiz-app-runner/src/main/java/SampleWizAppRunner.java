import be.fluid_it.tools.dw.wiz2war.ci.app.HelloWorldApplication;
import be.fluid_it.tools.dw.wiz2war.it.support.WizRunner;

public class SampleWizAppRunner {
    public static void main(String[] args) throws Exception {
        new WizRunner(new HelloWorldApplication(), "./target/classes/hello-world-as-dw-jar.yml").run();
    }
}