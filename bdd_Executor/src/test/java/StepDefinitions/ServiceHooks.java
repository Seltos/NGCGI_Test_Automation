package StepDefinitions;


import Base.BaseUtil;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;

public class ServiceHooks extends BaseUtil {
    private BaseUtil baseUtil;

    public ServiceHooks(){
        this.baseUtil=baseUtil;
    }

    @Before
    public void initializeTest(Scenario scenario){
        scenarioDef = BaseUtil.features.createNode(scenario.getName());
        System.out.println("Opening the browser : Firefox");
    }

    @After
    public void TearDownTest(io.cucumber.core.api.Scenario scenario) {
        if (scenario.isFailed()) {
            //Take screenshot logic goes here
            System.out.println(scenario.getName());
        }
        System.out.println("Closing the browser : MOCK");
        driver.quit();
    }

    @BeforeStep
    public void BeforeEveryStep(io.cucumber.core.api.Scenario scenario) {
        System.out.println("Before every step " + scenario.getId());

        //((PickleStep)((PickleStepTestStep)
    }

    @AfterStep
    public void AfterEveryStep(io.cucumber.core.api.Scenario scenario) throws NoSuchFieldException, IllegalAccessException {
        //System.out.println("Before every step " + stepTestStep.getId());
    }

}