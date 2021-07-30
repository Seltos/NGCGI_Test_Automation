package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchBy_Page_elements {
    public WebDriver driver;
    By username = By.xpath("//input[@id='userInput']");
    By Next_button = By.xpath("//input[@id='login-button']");
    By password = By.xpath("//input[@id='passwordInput']");
    By NGCGI_Title=By.xpath("//label[text()='Next Generation CGI (NG-CGI)']");
    By MaintainGoalSheet_link=By.xpath("//a[text()='Maintain Goal Sheet Info']");
    By ProvideGoalSheet_link=By.xpath("//div[@class='mat-menu-content ng-trigger ng-trigger-fadeInItems']/span[1]/button");
    By ProvideGoalSheet_heading=By.xpath("//label[@class='heading']");
    By Fiscalyear_attribute=By.xpath("//label[text()='Fiscal year']");
    By Fiscalyear_dropdown=By.xpath("//div[@class='row contentClass'][2]//div[2]//select");
    By emp_number=By.xpath("//input[@id='employeeNumber']");
    By emp_mail_id=By.xpath("//input[@name='emailId']");
    By emp_Name=By.xpath("//input[@name='empName']");
    By plan_code=By.xpath("//input[@name='planCode']");
    By Data_Provider =By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[2]/select");
    By RSE =By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[4]/select");
    By plan_proration_type =By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[6]/select");
    By node_level =By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[8]/select");
    By manager =By.xpath("//div[@class='panel panel-default']/div/form/div[3]/div[2]/select");
    By status =By.xpath("//div[@class='panel panel-default']/div/form/div[3]/div[4]/select");
    By emp_type =By.xpath("//div[@class='panel panel-default']/div/form/div[4]/div[2]/select");
    By search_btn =By.xpath("//span[text()='Search']");
    By clear_btn =By.xpath("//span[text()='Clear']");
    By emp_number_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[1]//div[1]/label");
    By emp_email_id_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[1]//div[3]/label");
    By emp_name_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[1]//div[5]/label");
    By emp_plan_code_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[1]//div[7]/label");
    By emp_data_provider_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[1]/label");
    By emp_rse_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[3]/label");
    By emp_plan_proration_type_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[5]/label");
    By emp_node_level_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[2]//div[7]/label");
    By emp_manager_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[3]//div[1]/label");
    By emp_status_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[3]//div[3]/label");
    By emp_goling_fiscal_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[3]//div[5]/label");
    By emp_node_name_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[3]//div[7]/label");
    By emp_type_Attribute=By.xpath("//div[@class='panel panel-default']/div/form/div[4]//div[1]/label");

    public SearchBy_Page_elements(WebDriver driver)
    {
        this.driver=driver;
    }
    public WebElement getUsername()
    {
        return driver.findElement(username);
    }
    public WebElement getNext_button()
    {
        return driver.findElement(Next_button);
    }
    public WebElement getPassword()
    {
        return driver.findElement(password);
    }
    public WebElement getNGCGI_Title()
    {
        return driver.findElement(NGCGI_Title);
    }
    public WebElement getMaintainGoalSheet_link()
    {
        return driver.findElement(MaintainGoalSheet_link);
    }
    public WebElement getProvideGoalSheet_link()
    {
        return driver.findElement(ProvideGoalSheet_link);
    }
    public WebElement getProvideGoalSheet_heading()
    {
        return driver.findElement(ProvideGoalSheet_heading);
    }
    public WebElement getFiscalyear_attribute()
    {
        return driver.findElement(Fiscalyear_attribute);
    }
    public WebElement getFiscalyear_dropdown()
    {
        return driver.findElement(Fiscalyear_dropdown);
    }
    public WebElement getEmp_number()
    {
        return driver.findElement(emp_number);
    }
    public WebElement getEmp_mail_id()
    {
        return driver.findElement(emp_mail_id);
    }
    public WebElement getEmp_Name()
    {
        return driver.findElement(emp_Name);
    }
    public WebElement getPlan_code()
    {
        return driver.findElement(plan_code);
    }
    public WebElement getData_Provider()
    {
        return driver.findElement(Data_Provider);
    }
    public WebElement getRSE()
    {
        return driver.findElement(RSE);
    }
    public WebElement getPlan_Proration_type()
    {
        return driver.findElement(plan_proration_type);
    }
    public WebElement getNode_level()
    {
        return driver.findElement(node_level);
    }
    public WebElement getManager()
    {
        return driver.findElement(manager);
    }
    public WebElement getStatus()
    {
        return driver.findElement(status);
    }
    public WebElement getEmp_type()
    {
        return driver.findElement(emp_type);
    }
    public WebElement getSearch_btn()
    {
        return driver.findElement(search_btn);
    }
    public WebElement getClear_btn()
    {
        return driver.findElement(clear_btn);
    }
    public WebElement getemp_number_Attribute()
    {
        return driver.findElement(emp_number_Attribute);
    }
    public WebElement getemp_email_id_Attribute()
    {
        return driver.findElement(emp_email_id_Attribute);
    }
    public WebElement getemp_name_Attribute()
    {
        return driver.findElement(emp_name_Attribute);
    }
    public WebElement getemp_plan_code_Attribute()
    {
        return driver.findElement(emp_plan_code_Attribute);
    }
    public WebElement getemp_data_provider_Attribute()
    {
        return driver.findElement(emp_data_provider_Attribute);
    }
    public WebElement getemp_rse_Attribute()
    {
        return driver.findElement(emp_rse_Attribute);
    }
    public WebElement getemp_plan_proration_type_Attribute()
    {
        return driver.findElement(emp_plan_proration_type_Attribute);
    }
    public WebElement getemp_node_level_Attribute()
    {
        return driver.findElement(emp_node_level_Attribute);
    }
    public WebElement getemp_manager_Attribute()
    {
        return driver.findElement(emp_manager_Attribute);
    }
    public WebElement getemp_status_Attribute()
    {
        return driver.findElement(emp_status_Attribute);
    }
    public WebElement getemp_goling_fiscal_Attribute()
    {
        return driver.findElement(emp_goling_fiscal_Attribute);
    }
    public WebElement getemp_node_name_Attribute()
    {
        return driver.findElement(emp_node_name_Attribute);
    }
    public WebElement getemp_type_Attribute()
    {
        return driver.findElement(emp_type_Attribute);
    }

}

