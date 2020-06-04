package StepDefinitions;


import cucumber.api.PendingException;
        import cucumber.api.java.en.Given;
        import cucumber.api.java.en.Then;
        import cucumber.api.java.en.When;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.AbstractList;
import java.util.Iterator;

public class StepDefinations {
    @Given("^I am on the \"([^\"]*)\" page on URL \"([^\"]*)\"$")
    public void i_am_on_the_page_on_URL(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.setProperty("webdriver.chrome.driver", "C:\\Softwares\\Drivers\\chromedriver.exe");

        // Instantiate a ChromeDriver class.
        WebDriver driver=new ChromeDriver();
       //Maximize the browser
        driver.manage().window().maximize();
        System.out.println("I am opening chrome Brwser");
        System.out.println("I am on the Page of Url");
        throw new PendingException();
    }

    @When("^I fill in \"([^\"]*)\" with \"([^\"]*)\"$")
    public void i_fill_in_with(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("I fill with ");
        throw new PendingException();
    }

    @When("^I click on the \"([^\"]*)\" button$")
    public void i_click_on_the_button(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("I click on buttons ");
        throw new PendingException();
    }

    @Then("^I should see \"([^\"]*)\" message$")
    public void i_should_see_message(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("I should see message");
        throw new PendingException();
    }


    @Given("^I open a \"([^\"]*)\"$")
    public void iOpenA(String brow) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.setProperty("webdriver.chrome.driver", "C:\\Softwares\\Drivers\\chromedriver.exe");
//
//        // Instantiate a ChromeDriver class.
        WebDriver driver=new ChromeDriver();
//
//        // Launch Website
//        driver.navigate().to("http://www.javatpoint.com/");
//
//        //Maximize the browser
//        driver.manage().window().maximize();
        System.out.println("I am opening chrome Brwser");
//       if(browser=="chrome")
//       {
////           System.out.println("RunMode is"+runmode);
////           WebDriverFactory driverFactory = new WebDriverFactory();
////           driver = driverFactory.getDriver(browser, runmode);
//           // System Property for Chrome Driver
//
//       }

      //  throw new PendingException();
    }

    @Given("^I open a Browser in Window$")
    public void iOpenABrowserInWindow() {
        System.out.println("I am opening chrome Brwser");
        System.setProperty("webdriver.chrome.driver", "C:\\Softwares\\Drivers\\chromedriver.exe");
//
//        // Instantiate a ChromeDriver class.
        WebDriver driver=new ChromeDriver();
//
//        // Launch Website
        driver.navigate().to("http://www.javatpoint.com/");
//
//        //Maximize the browser
        driver.manage().window().maximize();
        System.out.println("I am opening chrome Brwser");
    }
}