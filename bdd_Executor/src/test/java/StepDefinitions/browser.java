package StepDefinitions;

import Base.BaseUtil;
import Base.base;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import pages.CreateStory;
import pages.HomePage;
import pages.loginpage;
import org.openqa.selenium.JavascriptExecutor;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;


import java.io.IOException;

public class browser extends BaseUtil {
    //  public WebDriver driver = null;
    public loginpage login = null;
    public HomePage homepage = null;
    public CreateStory createStory = null;
    public String pass = "Qazxsw12";

    @Given("^I open application in \"([^\"]*)\" browser$")
    public void i_open_application_in_browser(String browser) throws Throwable {
        scenarioDef.createNode(new GherkinKeyword("Given"), "I open application");
        driver = base.initializeDriver();
      //  driver.get(prop.getProperty("url"));
       driver.manage().window().maximize();
    }



    @Given("^I close web browser$")
    public void i_close_web_browser() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        driver.close();
    }

    @When("^I type text \"([^\"]*)\" into UserName Field$")
    public void iTypeTextIntoUserNameField(String UserName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        scenarioDef.createNode(new GherkinKeyword("When"), "I type text in username field");
      System.out.println("UserName is  "+UserName);
        login = new loginpage(driver);
        login.getLogin_UserName().sendKeys(UserName);

    }

    @When("^I type text Password into Password Field$")
    public void iTypeTextPasswordIntoPasswordField() {
        login = new loginpage(driver);
        login.getLogin_PassWord().sendKeys(pass);
    }

    @And("^I click Submit Button$")
    public void iClickSubmitButton() throws Throwable{
        scenarioDef.createNode(new GherkinKeyword("And"), "I click Submit Button");
        login = new loginpage(driver);
        login.getLogin_Submit().click();
    }

    @When("^I Navigate to the url \"([^\"]*)\"$")
    public void iNavigateToTheUrl(String url) throws Throwable {
        scenarioDef.createNode(new GherkinKeyword("When"), "I Navigate to the url");
        // Write code here that turns the phrase above into concrete action
        driver.navigate().to(url);
       // Thread.sleep(30000);
    }

    @Then("^It Navigates to the Homepage$")
    public void itNavigatesToTheHomepage() throws IOException {
        ExtentTest logInfo=null;

        WebDriverWait titleContains = new WebDriverWait(driver, 50);
       String actualTitle=driver.getTitle();
       System.out.println(actualTitle);
        String expectedTitle="Log in to continue - Log in with Atlassian account";
        titleContains.until(ExpectedConditions.titleContains(expectedTitle));
        Assert.assertEquals(actualTitle, expectedTitle);
//        logInfo.pass("navigatetoHomepage");
//        logInfo.addScreenCaptureFromPath(captureScreenShot(driver));

//        logInfo.pass("test");
//        logInfo.addScreenCaptureFromPath(captureScreenShot(driver));

    }

    @Given("^I open the Grid in Jira Software$")
    public void iOpenTheGridInJiraSoftware() throws Throwable{
        scenarioDef.createNode(new GherkinKeyword("Given"), "I open the Grid in Jira Software");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        homepage = new HomePage(driver);
//        WebElement ele=homepage.getHomePage_Jira();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(homepage.getHomePage_Jele()));
//        homepage.getHomePage_Jira().click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(homepage.getHomePage_JSoftele()));
//        homepage.getHomePage_Software().click();
    }

    @Then("^I logout from application$")
    public void logout() throws InterruptedException {
        Thread.sleep(1600);
        WebDriverWait clickableWait = new WebDriverWait(driver, 25);
        login = new loginpage(driver);
        login.getLogout_Profile().click();
        login.getLogout().click();
        clickableWait.until(ExpectedConditions.elementToBeClickable(login.getLogout_Submit()));
        login.getLogout_Submit().click();
        System.out.println("Logged out from application");

    }
    @When("^I click on Enquero link$")
    public void iClickOnEnqueroLink() throws Throwable{
        scenarioDef.createNode(new GherkinKeyword("Given"), "I click on Enquero link");
        homepage = new HomePage(driver);
//        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 60);
//        elementToBeVisible.until(ExpectedConditions.visibilityOf(homepage.getHomePage_EnqLink()));
//        homepage.getHomePage_EnqLink().click();

    }

    @Then("^I see create Button and Enter the Details$")
    public void iSeeCreateButtonAndEnterTheDetails() throws Throwable{
        scenarioDef.createNode(new GherkinKeyword("Then"), "I see create Button and Enter the Details");
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 30);
       // elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Button()));
      //  WebDriverWait elementToBeVisible1 = new WebDriverWait(driver, 30);
//        elementToBeVisible1.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_YourWork_Button()));
//        createStory.getCreateStory_YourWork_Button().click();

    }




    @And("^I create the Story with Type \"([^\"]*)\"$")
    public void iCreateTheStoryWithType(String Type) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);

    }
    @And("^I create the Story with the Type \"([^\"]*)\"$")
    public void iCreateTheStoryWithTheType(String Type1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 60);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Button1()));
        WebDriverWait clickableWait = new WebDriverWait(driver, 50);
        clickableWait.until(ExpectedConditions.elementToBeClickable(createStory.getCreateStory_Button1()));
        Boolean bool=createStory.getCreateStory_Button1().isDisplayed();
        System.out.println("Button Clicked"+bool);
        Thread.sleep(2000);
        createStory.getCreateStory_Button1().click();
        System.out.println("Creation Button Clicked");

        WebDriverWait elementToBeVisible1 = new WebDriverWait(driver, 15);
        elementToBeVisible1.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_IssueType_Input()));
        createStory.getCreateStory_IssueType_Input().clear();
        createStory.getCreateStory_IssueType_Input().click();
        createStory.getCreateStory_IssueType_Input().sendKeys(Type1);
        System.out.println("Selected"+Type1);
        //createStory.getCreateStory_IssueType_Input().sendKeys(Keys.ENTER);
    }

    @Then("^I add Summary to the Story as \"([^\"]*)\"$")
    public void iAddSummaryToTheStoryAs(String summary) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 40);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Summary_Input()));
      //  createStory.getCreateStory_Summary_Input().clear();
        createStory.getCreateStory_Summary_Input().click();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,50)");
        Thread.sleep(5000);
        jse.executeScript("arguments[0].value='"+ summary +"';", createStory.getCreateStory_Summary_Input());
        System.out.println("SUMMARY"+summary);
        int Rand=base.Random_number_Generator();
        String RandStr="  "+Rand;
        createStory.getCreateStory_Summary_Input().sendKeys(RandStr);
        Thread.sleep(5000);
    }

    @Then("^I add Description to the Story as \"([^\"]*)\"$")
    public void iAddDescriptionToTheStoryAs(String description) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 15);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Description_textArea()));
       // createStory.getCreateStory_Description_textArea().clear();
         createStory.getCreateStory_Description_textArea().click();
//        createStory.getCreateStory_Description_textArea().sendKeys(description);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].value='"+ description +"';", createStory.getCreateStory_Description_textArea());
        System.out.println("Desc"+description);
    }

    @And("^I assign Priority as \"([^\"]*)\"$")
    public void iAssignPriorityAs(String priority) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 15);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Priority_Input()));
        //createStory.getCreateStory_Priority_Input().clear();
        createStory.getCreateStory_Priority_Input().click();
        //createStory.getCreateStory_Priority_Input().sendKeys(priority);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].value='"+ priority +"';", createStory.getCreateStory_Priority_Input());
        System.out.println("Pri"+priority);
    }

    @Then("^I set DueDate as \"([^\"]*)\"$")
    public void iSetDueDateAs(String DueDATE) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 15);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_DueDate_Input()));
        createStory.getCreateStory_DueDate_Input().clear();
        createStory.getCreateStory_DueDate_Input().click();
        //createStory.getCreateStory_DueDate_Input().sendKeys(DueDATE);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].value='"+ DueDATE +"';", createStory.getCreateStory_DueDate_Input());
        System.out.println("Date"+DueDATE);
    }

    @And("^I story related to Environoment \"([^\"]*)\"$")
    public void iStoryRelatedToEnvironoment(String Env) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 15);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Env_TextArea()));
        createStory.getCreateStory_Env_TextArea().clear();
        createStory.getCreateStory_Env_TextArea().click();
        createStory.getCreateStory_Env_TextArea().sendKeys(Env);
    }

    @And("^I assign Story to the Person \"([^\"]*)\"$")
    public void iAssignStoryToThePerson(String Assignee) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        createStory = new CreateStory(driver);
        WebDriverWait elementToBeVisible = new WebDriverWait(driver, 15);
        elementToBeVisible.until(ExpectedConditions.visibilityOf(createStory.getCreateStory_Assignee()));
        //createStory.getCreateStory_Assignee().clear();
        createStory.getCreateStory_Assignee().click();
        //createStory.getCreateStory_Assignee().sendKeys(Assignee);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].value='"+ Assignee +"';", createStory.getCreateStory_Assignee());
        Thread.sleep(3000);
        System.out.println("Assignee"+Assignee);

    }

    @Then("^Story is Created$")
    public void storyIsCreated()  throws Throwable  {

            WebDriverWait clickableWait = new WebDriverWait(driver, 15);
            clickableWait.until(ExpectedConditions.elementToBeClickable(createStory.getCreateStory_Create_Submit()));
            createStory.getCreateStory_Create_Submit().submit();
             Thread.sleep(3000);
            System.out.println("Submitted");
    }

    @AfterClass
    public void close()
    {
        driver.quit();
    }


    @Then("^Bug is Created$")
    public void bugIsCreated() {
        WebDriverWait clickableWait = new WebDriverWait(driver, 15);
        clickableWait.until(ExpectedConditions.elementToBeClickable(createStory.getCreateStory_Create_Submit()));
        createStory.getCreateStory_Create_Submit().submit();
        System.out.println("Submitted");

    }

}
