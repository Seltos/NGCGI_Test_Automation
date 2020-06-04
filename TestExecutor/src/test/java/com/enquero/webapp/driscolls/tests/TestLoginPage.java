package com.enquero.webapp.driscolls.tests;

import com.aventstack.extentreports.Status;

import com.enquero.Testlogs.GenerateLogs;
import com.enquero.datafactory.DataFactory.TestDataFactory;
import com.enquero.datafactory.xlsfile.ReadXlsFile;
import com.enquero.driverfactory.web.WebDriverFactory;
import com.enquero.reporter.ExtentTestReporter;
import io.qameta.allure.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

@Epic("Regression Tests")
@Feature("TestLogin Page Scenarios")
public class TestLoginPage {

    Logger logger= Logger.getLogger(TestLoginPage.class.getName());
    WebDriver driver;
    String path= System.getProperty("user.dir")+System.getProperty("file.separator")+"src\\main\\resources\\testData.xlsx";

    @DataProvider(name="getTestData", parallel=false)
    public Iterator<Object[]> getTestData(Method m) throws IOException {
        ReadXlsFile rd = new ReadXlsFile();
        return rd.getTestData(path,"testcase",m.getName());
    }

    @Parameters({ "browser", "webRunMode" })
    @BeforeClass
    public void setup(@Optional("chrome") String browser, @Optional ("local") String webRunMode) throws IOException {
        WebDriverFactory driverFactory = new WebDriverFactory();
        driver = driverFactory.getDriver(browser, webRunMode);
        GenerateLogs.loadLogPropertyFile();
        logger.info(Thread.currentThread().getName()+" ************* Execution Started *************");
    }

    @Test(dataProvider="getTestData",priority = 1)
    @Severity(SeverityLevel.MINOR)
    @Description("Test Case description: verify Javscript page")
    @Story("Story name:to check JS of the page")
    public void testLoginJavascript(TestDataFactory dataFactory) {
        logger.info(Thread.currentThread().getName() +" Javascript tc started");
        driver.get("https://www.protractortest.org/#/");
        System.out.println(dataFactory.getInputParameters());
        System.out.println(dataFactory.getValidationParameters());
        logger.info(Thread.currentThread().getName() +" Validation successfull for First tc");
        ExtentTestReporter.getTest().log(Status.INFO,"launched Javascript url...");
        ExtentTestReporter.getTest().log(Status.PASS,"Validated page title successfully");
        logger.error(Thread.currentThread().getName()+" validation failed");
        Assert.assertEquals("Swati","Chetty");
        logger.error(Thread.currentThread().getName()+" validation failed");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Test Case description: verify login page of Jasmine")
    @Story("Story name:to check title of the page")
    @Test(dataProvider="getTestData", description="Verifying login page titleof jasmine", priority = 0)
    public void testLoginJasmine(TestDataFactory dataFactory) {
        System.out.println("data path is "+path);
        logger.info(Thread.currentThread().getName()+" Testloginjasmine Test case started");
        ExtentTestReporter.getTest().log(Status.INFO,"Inside Test Method...");
        System.out.println(dataFactory.getInputParameters());
        logger.warn(Thread.currentThread().getName()+" Input Parameters not validated");
        String username=dataFactory.getInputParameters().get("username").toString();
        System.out.println(dataFactory.getValidationParameters());
        Assert.assertEquals(username,"SwatiChetty");
        logger.info(Thread.currentThread().getName()+" Username validated successfully");
        driver.get("https://jasmine.github.io/");
        ExtentTestReporter.getTest().log(Status.INFO,"launched Jasmine url...");
        Assert.assertEquals("Swati","Chetty");
        ExtentTestReporter.getTest().log(Status.PASS,"Validated url successfully");
        logger.info(Thread.currentThread().getName()+" testloginjasmine Testcase ended");
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Test Case description: verify Selenium page")
    @Story("Story name:to check Selenium of the page")
    @Test(dataProvider="getTestData",priority = 2)
    public void testLoginSelenium(TestDataFactory dataFactory) {
        logger.info(Thread.currentThread().getName() +" Selenium tc started");
        driver.get("https://www.selenium.dev/");
        System.out.println(dataFactory.getInputParameters());
        System.out.println(dataFactory.getValidationParameters());
        logger.info(Thread.currentThread().getName() +" Validation successfull for third tc");
        ExtentTestReporter.getTest().log(Status.INFO,"launched Selenium url...");
        ExtentTestReporter.getTest().log(Status.PASS,"Validated page title successfully");
        logger.error(Thread.currentThread().getName()+" validation failed");
        logger.error(Thread.currentThread().getName()+" validation failed");
    }

    @AfterClass
    public  void tearDown(){
        logger.info(Thread.currentThread().getName()+" ************* Execution Ended *************");
        driver.quit();
    }

}
