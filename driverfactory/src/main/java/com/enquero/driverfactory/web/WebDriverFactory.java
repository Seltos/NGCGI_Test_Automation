package com.enquero.driverfactory.web;

import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class WebDriverFactory {

    //public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private static WebDriver driver;
    public WebDriverFactory() {

    }


    public WebDriver getDriver(String driverType, String webRunMode) throws MalformedURLException {
        if (webRunMode.equalsIgnoreCase("remote")) {
            driver = new RemoteDriverConfig().getRemoteDriver();
        } else {
            switch (driverType) {
                case "chrome":
                    driver = new ChromeDriverConfig().getChromeDriver();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "opera":
                    WebDriverManager.operadriver().setup();
                    driver = new OperaDriver();
                    break;
                case "phantom":
                    WebDriverManager.phantomjs().setup();
                    driver = (WebDriver) new PhantomJsDriverManager().chromedriver();
                    break;
            }
        }
        return driver;
    }

    public static WebDriver getDriverinstance(){
        return driver;
    }
}