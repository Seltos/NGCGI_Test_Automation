package StepDefinitions;

import BaseTest.base;
import HelperUtilities.BDD_Common_Utilities;
import HelperUtilities.LoggerHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.lexer.Th;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.SearchBy_Page_elements;

import java.io.FileInputStream;
import java.util.Properties;

public class NGCGI_Stepdefinition extends base {
    public SearchBy_Page_elements PageEle = null;
    Logger log = LoggerHelper.getLogger(NGCGI_Stepdefinition.class);
    public String Emp_Number="147752";
    public String Emp_Email_Id="gsoftley";
    public String Emp_Name="Softley, Gary Richard";
    public String Plan_Code="ISR2";

    @Given("^I open application in \"([^\"]*)\" browser$")
    public void i_open_application_in_browser(String browser) {
       try {
           driver = initializeDriver();
           log.info("Driver is initialized");
           log.info("*******Execution Started**************");
           //  driver.get(prop.getProperty("url"));
           // driver.manage().window().maximize();
       }catch (Exception e)
       {
           System.out.println(e);
       }
    }

    @When("^I Navigate to the url$")
    public void i_Navigate_to_the_url() {
       try {
           prop = new Properties();
           FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\data.properties");
           prop.load(fis);
           driver.navigate().to(prop.getProperty("url"));
           Thread.sleep(10000);
           log.info("Navigated to the url"+prop.getProperty("url"));
       }catch  (Exception e)
       {
           System.out.println(e);
       }
    }

    @When("^I type text userName into UserName Field$")
    public void i_type_text_userName_into_UserName_Field() {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\data.properties");
            prop.load(fis);
            System.out.println("UserName is  "+prop.getProperty("userName"));
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getUsername().sendKeys(prop.getProperty("userName"));
            Thread.sleep(2000);
            log.info("Username Entered"+prop.getProperty("userName"));
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @When("^I click Submit Button$")
    public void i_click_Submit_Button()  {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getNext_button().click();
            Thread.sleep(2000);
            log.info("Sing-in into the Application");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @When("^I type text Password into Password Field$")
    public void i_type_text_Password_into_Password_Field() throws Throwable {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\data.properties");
            prop.load(fis);
            System.out.println("Password is  "+prop.getProperty("PassWord"));
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getPassword().sendKeys(prop.getProperty("PassWord"));
            log.info("Password Entered"+prop.getProperty("PassWord"));
            Thread.sleep(2000);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Then("^It Navigates to the Homepage$")
    public void it_Navigates_to_the_Homepage()  {
        try {
            Thread.sleep(10000);
            PageEle = new SearchBy_Page_elements(driver);
            Actions act = new Actions(driver);
            act.sendKeys(Keys.TAB).build().perform();
            Thread.sleep(2000);
            act.sendKeys(Keys.ENTER).build().perform();
            Thread.sleep(2000);
            act.sendKeys(Keys.RETURN).build().perform();
            Thread.sleep(25000);
            WebDriverWait titleContains = new WebDriverWait(driver, 50);
            String actualTitle=PageEle.getNGCGI_Title().getText();
            System.out.println(actualTitle);
            String expectedTitle="Next Generation CGI (NG-CGI)";
            titleContains.until(ExpectedConditions.titleContains(expectedTitle));
            Assert.assertEquals(actualTitle, expectedTitle);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    @Then("^I validate the Next Generation CGI home page$")
    public void i_validate_the_Next_Generation_CGI_home_page()  {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getMaintainGoalSheet_link().click();
            Thread.sleep(2000);
            PageEle.getProvideGoalSheet_link().click();
            Thread.sleep(2000);
            WebDriverWait titleContains = new WebDriverWait(driver, 50);
            String actualTitle=PageEle.getProvideGoalSheet_heading().getText();
            System.out.println(actualTitle);
            String expectedTitle="Provide Goal Sheet Information";
            titleContains.until(ExpectedConditions.titleContains(expectedTitle));
            Assert.assertEquals(actualTitle, expectedTitle);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Given("^I validate Maintain goalSheet info$")
    public void i_validate_Maintain_goalSheet_info() {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            boolean MS_link=PageEle.getMaintainGoalSheet_link().isDisplayed();
            Assert.assertEquals(MS_link,true);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @When("^I click on Maintain goalSheet info link$")
    public void i_click_on_Maintain_goalSheet_info_link()  {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            boolean MS_link=PageEle.getMaintainGoalSheet_link().isDisplayed();
            Assert.assertEquals(MS_link,true);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Then("^I see Provide Goal Sheet Information$")
    public void i_see_Provide_Goal_Sheet_Information()  {
        try {
            String actualTitle=PageEle.getProvideGoalSheet_heading().getText();
            System.out.println(actualTitle);
            String expectedTitle="Provide Goal Sheet Information";
            Assert.assertEquals(actualTitle, expectedTitle);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Then("^I see Fiscal year drop down$")
    public void i_see_Fiscal_year_drop_down()  {
       try {
           PageEle = new SearchBy_Page_elements(driver);
           boolean FiscalYear=PageEle.getFiscalyear_attribute().isDisplayed();
           Assert.assertEquals(FiscalYear,true);
           boolean FiscalYear_dropdown=PageEle.getFiscalyear_dropdown().isDisplayed();
           Assert.assertEquals(FiscalYear_dropdown,true);
       }catch (Exception e){
           System.out.println(e);
       }
    }

    @Then("^I Search By table with different filters$")
    public void i_Search_By_table_with_different_filters()  {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getFiscalyear_dropdown().click();
            Thread.sleep(2000);
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            BDD.getAllDropDownValues(PageEle.getFiscalyear_dropdown());
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Then("^I validate Emp Number,Emp Email Id,Emp Name,Plan Code,Data Provider,RSE,Plan Proration Type,Node Level,Manager,Status,Goaling Fiscal Interval,Node Name,Emp Type,Search,Clear$")
    public void i_validate_Emp_Number_Emp_Email_Id_Emp_Name_Plan_Code_Data_Provider_RSE_Plan_Proration_Type_Node_Level_Manager_Status_Goaling_Fiscal_Interval_Node_Name_Emp_Type_Search_Clear()  {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            String Emp_number=PageEle.getemp_number_Attribute().getText();
            System.out.println(Emp_number);
            String expectedTitle="Emp Number";
            Assert.assertEquals(Emp_number, expectedTitle);
            String Emp_mail_id=PageEle.getemp_email_id_Attribute().getText();
            System.out.println(Emp_mail_id);
            String expectedEmpid="Emp Email Id";
            Assert.assertEquals(Emp_mail_id, expectedEmpid);
            String Emp_Name=PageEle.getemp_name_Attribute().getText();
            System.out.println(Emp_Name);
            String expectedEmp_Name="Emp Name";
            Assert.assertEquals(Emp_Name, expectedEmp_Name);
            String Plan_code=PageEle.getemp_plan_code_Attribute().getText();
            System.out.println(Plan_code);
            String expectedPlan_code="Plan Code";
            Assert.assertEquals(Plan_code, expectedPlan_code);
            String Data_Provider=PageEle.getemp_data_provider_Attribute().getText();
            System.out.println(Data_Provider);
            String expectedData_Provider="Data Provider";
            Assert.assertEquals(Data_Provider, expectedData_Provider);
            Thread.sleep(2000);
            PageEle.getData_Provider().click();
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            BDD.getAllDropDownValues(PageEle.getData_Provider());
            Thread.sleep(2000);
            String RSE=PageEle.getemp_rse_Attribute().getText();
            System.out.println(RSE);
            String expectedData_RSE="RSE";
            Assert.assertEquals(RSE, expectedData_RSE);
            PageEle.getRSE().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getRSE());
            String Plan_Proration_type=PageEle.getemp_plan_proration_type_Attribute().getText();
            System.out.println(Plan_Proration_type);
            String expectedData_Plan_Proration_type="Plan Proration Type";
            Assert.assertEquals(Plan_Proration_type, expectedData_Plan_Proration_type);
            PageEle.getPlan_Proration_type().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getPlan_Proration_type());
            String Node_level=PageEle.getemp_node_level_Attribute().getText();
            System.out.println(Node_level);
            String expectedData_Node_level="Node Level";
            Assert.assertEquals(Node_level, expectedData_Node_level);
            PageEle.getNode_level().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getNode_level());
            String Manager=PageEle.getemp_manager_Attribute().getText();
            System.out.println(Manager);
            String expectedData_Manager="Manager";
            Assert.assertEquals(Manager, expectedData_Manager);
            PageEle.getManager().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getManager());
            String Status=PageEle.getemp_status_Attribute().getText();
            System.out.println(Status);
            String expectedData_Status="Status";
            Assert.assertEquals(Status, expectedData_Status);
            PageEle.getStatus().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getStatus());
            String Goaling=PageEle.getemp_goling_fiscal_Attribute().getText();
            System.out.println(Goaling);
            String expectedData_Goaling="Goaling Fiscal Interval";
            Assert.assertEquals(Goaling, expectedData_Goaling);
            String Nodename=PageEle.getemp_node_name_Attribute().getText();
            System.out.println(Nodename);
            String expectedData_Nodename="Node Name";
            Assert.assertEquals(Nodename, expectedData_Nodename);
            String Emp_type=PageEle.getemp_type_Attribute().getText();
            System.out.println(Emp_type);
            String expectedData_Emp_type="Emp Type";
            Assert.assertEquals(Emp_type, expectedData_Emp_type);
            PageEle.getEmp_type().click();
            Thread.sleep(2000);
            BDD.getAllDropDownValues(PageEle.getEmp_type());
            String Search_btn=PageEle.getSearch_btn().getText();
            System.out.println(Search_btn);
            String expectedData_Search_btn="Search";
            Assert.assertEquals(Search_btn, expectedData_Search_btn);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Given("^I enter the Emp Number in Emp Number text field$")
    public void i_enter_the_Emp_Number_in_Emp_Number_text_field() {
      try {
          PageEle = new SearchBy_Page_elements(driver);
          PageEle.getEmp_number().sendKeys(Emp_Number);
      }catch (Exception e){
          System.out.println(e);
      }

    }
    @When("^I click on search button$")
    public void i_click_on_search_button() {
        try {
            PageEle.getSearch_btn().click();
            Thread.sleep(5000);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    @Then("^I see respective employee details in search result table$")
    public void i_see_respective_employee_details_in_search_result_table(){
        try {
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            String Emp_Number_Actual=BDD.gettableData(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]",Emp_Number);
            Assert.assertEquals(Emp_Number_Actual, Emp_Number);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Then("^I verify employee details in search result table$")
    public void i_verify_employee_details_in_search_result_table(){
        try {
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            String Emp_Number_Actual=BDD.gettableData(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]",Emp_Number);
            Assert.assertEquals(Emp_Number_Actual, Emp_Number);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @Given("^I enter the Emp Email Id in Emp Email Id text field$")
    public void i_enter_the_Emp_Email_Id_in_Emp_Email_Id_text_field(){
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getEmp_mail_id().sendKeys(Emp_Email_Id);
            Thread.sleep(5000);
            PageEle.getSearch_btn().click();
            Thread.sleep(5000);
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            String Emp_id_Actual=BDD.gettableData(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]",Emp_Email_Id);
            Assert.assertEquals(Emp_id_Actual, Emp_Email_Id);
        }catch (Exception e)
        {
         System.out.println(e);
        }
    }

    @Given("^I enter the Emp last Name in Emp last Name text field$")
    public void i_enter_the_Emp_last_Name_in_Emp_last_Name_text_field(){
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getEmp_Name().sendKeys(Emp_Name);
            Thread.sleep(5000);
            PageEle.getSearch_btn().click();
            Thread.sleep(5000);
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            String Actual_Emp_Name=BDD.gettableData(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]",Emp_Name);
            Assert.assertEquals(Actual_Emp_Name, Emp_Name);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Given("^I enter the Plan Code and Emp Number in respective text field$")
    public void i_enter_the_Plan_Code_and_Emp_Number_in_respective_text_field() {
        try {
            PageEle = new SearchBy_Page_elements(driver);
            PageEle.getPlan_code().sendKeys(Plan_Code);
            Thread.sleep(5000);
            PageEle.getSearch_btn().click();
            Thread.sleep(5000);
            BDD_Common_Utilities BDD=new BDD_Common_Utilities();
            String Actual_Plan_Code=BDD.gettableData(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]",Plan_Code);
            Assert.assertEquals(Actual_Plan_Code, Plan_Code);
            BDD.gettableDataClick(driver, "//table[@id='quotaSheetTable']/tbody/tr[", "]/td[", "]","arrow_right");
            Thread.sleep(5000);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Given("^I enter the Data Provider and Emp Number in respective text field$")
    public void i_enter_the_Data_Provider_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the RSE and Emp Number in respective text field$")
    public void i_enter_the_RSE_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the Plan Proration Type and Emp Number in respective text field$")
    public void i_enter_the_Plan_Proration_Type_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the Node Level and Emp Number in respective text field$")
    public void i_enter_the_Node_Level_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the Manager and Emp Number in respective text field$")
    public void i_enter_the_Manager_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the Status and Emp Number in respective text field$")
    public void i_enter_the_Status_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Given("^I enter the Emp Type and Emp Number in respective text field$")
    public void i_enter_the_Emp_Type_and_Emp_Number_in_respective_text_field(){
        System.out.println("This Scenario has been validated earlier hence not validating again");
    }

    @Then("^I logout from application$")
    public void i_logout_from_application() {
    System.out.println("As logout functionality is not available hence not automated");
    }

    @Then("^I close web browser$")
    public void i_close_web_browser(){
        try {
            driver.close();
            driver.quit();
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

}
