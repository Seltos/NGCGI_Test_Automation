package com.enquero.driverfactory.web;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.logging.Logger;

public class ChromeDriverConfig {

    //public Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public WebDriver getChromeDriver() {
        //WebDriverManager.chromedriver().version("79.0.3945.36").setup();
        WebDriverManager.chromedriver().version("81.0.4044.69").setup();
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        return chromeDriver;
    }
}
