package com.enquero.TestListener;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.enquero.api.driscolls.APICommon.JiraReusableUtility;
import com.enquero.driverfactory.web.WebDriverFactory;
import com.enquero.reporter.ExtentTestReporter;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.IOException;
import java.util.Iterator;

public class AllureExtentTestNGListener implements ITestListener, ISuiteListener {
    public static ExtentTest extent;
    public static String testName;
    public static String testCasename;

    public static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "page screenshot", type="image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Override
    public void onStart(ISuite suite) {
        try {
            ExtentTestReporter.cleanDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("***** Test Suite "+suite.getName()+" started *******");
    }


    @Override
    public void onFinish(ISuite suite) {
        System.out.println("Test Suite Finished: "+suite.getName());
        ExtentTestReporter.endTest();
    }

    @Override
    public void onTestStart(ITestResult result) {
        testCasename=result.getMethod().getMethodName();
        System.out.println("Running Test Method: "+result.getMethod().getMethodName());
        extent=ExtentTestReporter.startTest(result.getMethod().getMethodName());
        ExtentTestReporter.getTest().assignCategory("RegressionTests");
        ExtentTestReporter.getTest().log(Status.INFO,"Execution of "+result.getMethod().getMethodName()+" STARTED ");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Executed  "+result.getMethod().getMethodName()+" Succesfully .....");
        ExtentTestReporter.getTest().log(Status.PASS,"Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        //Added ExtentReports part
        System.out.println("I am in onTestFailure method " +  getTestMethodName(result) + " failed");
        String path = null;
        String testMethodName = result.getName().trim();
        if (!testName.toUpperCase().contains("API")) {
            try {
                //path=ExtentTestReporter.getScreenshot(WebDriverFactory.getDriverinstance(),testMethodName);
                path = ExtentTestReporter.getFullPageShutterbug(WebDriverFactory.getDriverinstance(), testMethodName);
            } catch (Exception e) {
                e.printStackTrace();
                ExtentTestReporter.getTest().info(("An exception occurred while taking screenshot " + e.getCause()));
            }
            String tag = "<a href=" + "\"" + path + "\"" + "target='_blank'" + ">" + "Click here" + "</a";
            System.out.print("tag name: " + tag);
            ExtentTestReporter.getTest().log(Status.FAIL, "Test Case failed and screenshot attached: " + tag);
        }
        final Throwable error= result.getThrowable();
        final String message= ExtentTestReporter.getCustomStackTrace(error);
        String tag1= "<a href=\"javascript:window.alert('"+message+"');\">Click here</a>";
        ExtentTestReporter.getTest().log(Status.FAIL,"Logs generated for failed step: "+tag1);
        //added Allure Report Part
        Object testClass = result.getInstance();
        WebDriver driver= WebDriverFactory.getDriverinstance();
        if (driver !=null){
            System.out.println("Screenshot captured for test case:" + getTestMethodName(result));
            saveScreenshotPNG(driver);
        }
        saveTextLog(getTestMethodName(result) + " testcase failed and screenshot taken!");
        Boolean flag= result.wasRetried();
        System.out.println("The test: "+result.getTestName()+"retried flag is: "+flag);
        String bugId= JiraReusableUtility.createIssue();
        ExtentTestReporter.getTest().log(Status.FAIL,"Bug Id created in Jira is: "+bugId);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
        //ExtentTestReporter.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        testName=context.getName();
        System.out.println("***** Test "+ context.getName() +" started *******");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("On Finish Context level executing");
        Iterator<ITestResult> failedTests = context.getFailedTests().getAllResults().iterator();
        Iterator<ITestResult> skippedTests = context.getSkippedTests().getAllResults().iterator();
        while (failedTests.hasNext()) {
            ITestResult failedTestCases = failedTests.next();
            ITestNGMethod method = failedTestCases.getMethod();
            if (context.getFailedTests().getResults(method).size() > 1) {
                failedTests.remove();
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    failedTests.remove();
                }
            }
        }
        while (skippedTests.hasNext()) {
            ITestResult skippedTestCases = skippedTests.next();
            ITestNGMethod method = skippedTestCases.getMethod();
            if (context.getSkippedTests().getResults(method).size() > 1) {
                skippedTests.remove();
            } else {
                if (context.getSkippedTests().getResults(method).size() > 0) {
                    skippedTests.remove();
                }
            }
        }
    }
}
