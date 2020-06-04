package StepDefinitions;


import BaseTest.base;
import com.cucumber.listener.Reporter;
import com.enquero.api.driscolls.APICommon.JiraReusableUtility;
import cucumber.api.Scenario;
        import cucumber.api.java.After;
        import cucumber.api.java.Before;
import HelperUtilities.LoggerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class ServiceHooks extends base {
    public Scenario scenario;
    String Bugid;
    Logger log = LoggerHelper.getLogger(ServiceHooks.class);
    @Before
    public void initializeTest(Scenario scenario) {
        this.scenario = scenario;
    }

    @After
    public void embedScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
                try {
                    log.info(scenario.getName() + " is Failed");
                    final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    File screenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String testName = scenario.getName();
                    System.out.println(testName+"FAILED");
                    //Cucumber pretty
                    scenario.embed(screenshot, "image/png");
                    scenario.write(testName);
                    File DestinationPath= new File(System.getProperty("user.dir")+"\\reports\\Screenshots\\"+testName+"_" + System.currentTimeMillis() + ".png");
                    FileUtils.copyFile(screenshot1, new File(System.getProperty("user.dir")+"\\reports\\Screenshots\\"+testName+"_" + System.currentTimeMillis() + ".png"));
                    Reporter.addScreenCaptureFromPath(DestinationPath.toString());
                    Reporter.addScreenCast(DestinationPath.toString());
                    Reporter.addScenarioLog(scenario+"FAILED");
                    //Call API Module in this File
                    log.info(testName +"On this test failure it started cretaing bug");
                  Bugid=JiraReusableUtility.createIssue(testName);
                    log.info(scenario.getName() + " is Failed");
                    if(Bugid.isEmpty())
                    {
                        Reporter.addStepLog(scenario.getName()+" : Test Case Execution is successfull");

                    }else {
                        Reporter.addStepLog(scenario.getName()+" : Test Case Execution is Failed");
                        Reporter.addStepLog("BugId Created is :"+Bugid);
                        log.info(testName + "On this test failure it started created bug" + Bugid);
                        System.out.println(testName + "On this test failure it started created bug" + Bugid);
                        Reporter.addScreenCaptureFromPath(DestinationPath.toString());
                    }
                } catch (ClassCastException | IOException cce) {
                    cce.printStackTrace();
                // Code to capture and embed images in test reports (if scenario fails)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            log.info(scenario.getName() + " is pass");
        }
    }
}