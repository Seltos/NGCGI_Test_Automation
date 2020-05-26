package com.enquero.webapp.driscolls.tests;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import com.enquero.datafactory.csvfile.ReadCSVFile;
import com.enquero.driverfactory.web.WebDriverFactory;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Iterator;

public class Testlogincsv {

    WebDriver driver;
    String path= System.getProperty("user.dir")+System.getProperty("file.separator")+"src\\main\\resources\\testData.csv";

    @DataProvider(name="getTestData", parallel=false)
    public Iterator<Object[]> getTestData(Method m) throws IOException {
        System.out.println("started");
        ReadCSVFile rd = new ReadCSVFile();
        return rd.getTestData(path,"testLoginFacebook");
    }

    @Parameters({ "browser", "webRunMode" })
    @BeforeClass
    public void setup(@Optional ("chrome") String browser,@Optional ("local") String runmode) throws MalformedURLException {
        System.out.println("RunMode is"+runmode);
        WebDriverFactory driverFactory = new WebDriverFactory();
        driver = driverFactory.getDriver(browser, runmode);
    }

    //@Test(dataProvider="getTestData")
    public void testLogin(TestDataFactory dataFactory)
    {
        System.out.println("Inside Method Test Money control");
        System.out.println(dataFactory.getTestCaseId());
        System.out.println(dataFactory.getTestCaseName());
        System.out.println(dataFactory.getInputParameters());
        JSONObject obj=dataFactory.getInputParameters();
        String username=obj.get("username").toString();
        System.out.println(username);
        System.out.println(dataFactory.getValidationParameters());
        driver.get("https://www.facebook.com/");
    }

    //@Test(dataProvider="getTestData")
    public void testLoginFacebook(TestDataFactory dataFactory) {
        System.out.println("Inside Method Test Facebook");
        driver.get("https://www.seleniumeasy.com/");
        System.out.println(dataFactory.getInputParameters());
        System.out.println(dataFactory.getValidationParameters());
    }

    @AfterClass
    public  void tearDown(){
        driver.quit();
    }


}
