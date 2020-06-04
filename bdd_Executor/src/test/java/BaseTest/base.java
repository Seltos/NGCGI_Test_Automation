package BaseTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class base {

        public static WebDriver driver;
        public Properties prop;
        public static WebDriverWait wait;

        public WebDriver initializeDriver() throws IOException {

            prop = new Properties();
//            FileInputStream fis = new FileInputStream(
//                    System.getProperty("user.dir")+"\\TestExecutor\\src\\main\\resources\\data.properties");
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\data.properties");
            prop.load(fis);
            String browserName = prop.getProperty("browser");
            System.out.println(browserName);

            if (browserName.equals("chrome")) {
                WebDriverManager.chromedriver().setup();
                String Url = "www.google.com";
                ChromeOptions options = new ChromeOptions();
                options.addArguments("start-maximized");
                options.addArguments("enable-automation");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-browser-side-navigation");
                options.addArguments("-disable-gpu");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.addArguments("--disable-web-security");
                options.addArguments("--allow-running-insecure-content");
                driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                wait = new WebDriverWait(driver, 30);

            } else if (browserName.equals("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                driver.manage().window().maximize();

            } else if (browserName.equals("IE")) {

            }

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return driver;

        }
        public void getScreenshot(String result) throws IOException
        {
            File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("C://test//"+result+"screenshot.png"));

        }

    public static int Random_number_Generator() {
        Random objGenerator = new Random();
        int randomNumber=0;
        randomNumber = objGenerator.nextInt(100);
        System.out.println("Random No : " + randomNumber);
        return randomNumber;
    }


    }