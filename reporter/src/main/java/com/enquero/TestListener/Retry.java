package com.enquero.TestListener;

import com.aventstack.extentreports.Status;
import com.enquero.driverfactory.web.WebDriverFactory;
import com.enquero.reporter.ExtentTestReporter;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private static int retryCount=0;
    private static int maxRetryCount = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (retryCount < maxRetryCount) {
                System.out.println("Retrying test " + iTestResult.getName() + " with status " + getResultStatusName(iTestResult.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
                ExtentTestReporter.getTest().log(Status.INFO, "Retrying test for " + (retryCount + 1) + " time(s).");
                retryCount++;
                iTestResult.setStatus(ITestResult.FAILURE);
                extendReportsFailOperations(iTestResult);
                ExtentTestReporter.getInstance().removeTest(AllureExtentTestNGListener.extent);
                return true;
            }else{
                retryCount=0;
            }
        }else{
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }
    public String getResultStatusName(int status) {
        String resultName = null;
        if(status==1)
            resultName = "SUCCESS";
        if(status==2)
            resultName = "FAILURE";
        if(status==3)
            resultName = "SKIP";
        return resultName;
    }

    public void extendReportsFailOperations (ITestResult iTestResult) {
        Object testClass = iTestResult.getInstance();
        WebDriver webDriver = WebDriverFactory.getDriverinstance();
        String path = null;
        String testMethodName = iTestResult.getName().trim();
        if (AllureExtentTestNGListener.testName.toUpperCase().contains("API")) {
            try {
                path = ExtentTestReporter.getFullPageShutterbug(WebDriverFactory.getDriverinstance(), testMethodName);
            } catch (Exception e) {
                e.printStackTrace();
                ExtentTestReporter.getTest().info(("An exception occurred while taking screenshot " + e.getCause()));
            }
            String tag = "<a href=" + "\"" + path + "\"" + "target='_blank'" + ">" + "Click here" + "</a";
            System.out.print("tag name: " + tag);
            ExtentTestReporter.getTest().log(Status.FAIL, "Retried Test Case failed and screenshot attached: " + tag);
        }
        final Throwable error= iTestResult.getThrowable();
        final String message= ExtentTestReporter.getCustomStackTrace(error);
        String tag1= "<a href=\"javascript:window.alert('"+message+"');\">Click here</a>";
        ExtentTestReporter.getTest().log(Status.FAIL,"Logs generated for failed step: "+tag1);
        if (webDriver !=null){
            AllureExtentTestNGListener.saveScreenshotPNG(webDriver);
        }
        AllureExtentTestNGListener.saveTextLog(AllureExtentTestNGListener.getTestMethodName(iTestResult) + " Retried testcase failed and screenshot taken!");
    }
}
