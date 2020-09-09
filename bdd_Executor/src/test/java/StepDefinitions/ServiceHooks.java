package StepDefinitions;


import BaseTest.base;
import com.cucumber.listener.Reporter;
import com.enquero.api.driscolls.APICommon.JiraReusableUtility;
import com.enquero.reporter.ExtentTestReporter;
import cucumber.api.Scenario;
        import cucumber.api.java.After;
        import cucumber.api.java.Before;
import HelperUtilities.LoggerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ISuite;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ServiceHooks extends base {
    public static HashMap<String,String> hp= new HashMap<String,String>();
    public Scenario scenario;
    private static final String fileSeperator = System.getProperty("file.separator");
    private static Path root= Paths.get(System.getProperty("user.dir")).getParent();
    public static String reportFilePath=root+fileSeperator+"TestExecutor\\src\\Reports";
    public static int failedTCCount=0;
    public static int passedTCCount=0;
    public String suiteName;
    String Bugid;
    Logger log = LoggerHelper.getLogger(ServiceHooks.class);
    @Before
    public void initializeTest(Scenario scenario) {
        this.scenario = scenario;
    }

    public void getSuiteName(ISuite suite){
        suiteName=suite.getName();
    }

    @After
    public void embedScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
                try {
                    failedTCCount=failedTCCount+1;
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
                    Reporter.addScenarioLog(scenario+" FAILED");
                    //Call API Module in this File
                    log.info(testName +"On this test failure it started cretaing bug");
                    Bugid=JiraReusableUtility.createIssue(testName,"Regression Web Test Suite","");
                    hp.put(scenario.getName(),Bugid);
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
            passedTCCount=passedTCCount+1;
        }
    }


    public static void getTestDetails(){
        try {
            driver.quit();
            System.out.println("report path is: "+reportFilePath);
            ExtentTestReporter.addTestCountDetailsToFile(reportFilePath, "BDDWEB", hp, passedTCCount, failedTCCount);
        }catch (IOException e){
            e.printStackTrace();
        }

        }

}
