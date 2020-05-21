package ExtentReports_Listeners.TestListeners;

import ExtentReports_Listeners.ExtReporter.ExtentTestReporter;
import org.testng.*;
import com.aventstack.extentreports.gherkin.model.Feature;
import static Base.BaseUtil.features;
import java.io.IOException;



public class TestListener implements ITestListener, ISuiteListener {
    ExtentTestReporter extentTestReporter= new ExtentTestReporter();

    @Override
    public void onFinish(ISuite suite)
    {
        System.out.println("Test Suite Finished: "+suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Running Test Method: "+result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Executed  "+result.getMethod().getMethodName()+" Succesfully .....");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("On test failure");

        try{
            extentTestReporter.ExtentReportScreenshot();
        }catch (IOException e){

            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public  void onStart(ITestContext context) {
        System.out.println("On start");
        features= extentTestReporter.ExtentReport();
        System.out.println("features: "+features);
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("On finish");
        extentTestReporter.FlushReport();
    }

    @Override
    public void onStart(ISuite suite) {
        System.out.println("***** Test Suite "+suite.getName()+" started *******");
    }
}
