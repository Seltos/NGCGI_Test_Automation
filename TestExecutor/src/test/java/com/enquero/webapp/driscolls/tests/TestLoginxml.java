package com.enquero.webapp.driscolls.tests;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import com.enquero.datafactory.xmlfile.ReadXMLFile;
import com.enquero.driverfactory.web.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;

public class TestLoginxml {

    WebDriver driver;
    @DataProvider(name="getTestData", parallel=false)
    public Iterator<Object[]> getTestData(Method m) throws IOException {
        ReadXMLFile rd = new ReadXMLFile();
        return rd.getTestData("C:\\Enquero_Automation_Framework\\TestExecutor\\src\\main\\resources\\testData.xml","testGoogle");
    }

     @Parameters({ "browser", "webRunMode" })
    @BeforeClass
    public void setup(@Optional ("chrome") String browser,@Optional ("local") String runmode) throws  MalformedURLException {
        System.out.println("RunMode is"+runmode);
        WebDriverFactory driverFactory = new WebDriverFactory();
        driver = driverFactory.getDriver(browser, runmode);
    }

    //@Test(dataProvider="getTestData")
    public void testLogin(TestDataFactory dataFactory)
    {
        System.out.println("Inside Method Test Money control");
        System.out.println(dataFactory.getInputParameters());
        HashMap<Object, Object> obj=dataFactory.getInputParameters();
        String username=obj.get("username").toString();
        System.out.println(username);
        driver.get("https://www.moneycontrol.com/");
    }

    //@Test(dataProvider="getTestData")
    public void testLoginFacebook(TestDataFactory dataFactory) {
        ReadXMLFile rd = new ReadXMLFile();
        rd.getTestData("C:\\Enquero_Automation_Framework\\TestExecutor\\src\\main\\resources\\testData.xml","testcaseId");
    }

    @AfterClass
    public  void tearDown(){
        driver.quit();
    }
}
