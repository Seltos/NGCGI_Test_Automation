package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage
{
    public WebDriver driver;
    By HomePage_title=By.xpath("//title[text()='Atlassian | Start page']");
    By HomePage_Jira= By.xpath("(//div//span[@class='Icon__IconWrapper-dyhwwi-0 fLFsTG'])[1]");
    By HomePage_Software=By.xpath("//span[text()='Jira Software']");
    By HomePage_EnqLink=By.xpath("//span[text()='Enquero']");

   public HomePage(WebDriver driver)
    {
        this.driver=driver;
    }
    public By getHomePage_Jele()
    {
        return HomePage_Jira;
    }
    public By getHomePage_JSoftele()
    {
        return HomePage_Software;
    }
    public WebElement getHomePage_title()
    {
        return driver.findElement(HomePage_title);
    }
    public WebElement getHomePage_Jira()
    {
        return driver.findElement(HomePage_Jira);
    }
    public WebElement getHomePage_Software()
    {
        return driver.findElement(HomePage_Software);
    }
    public WebElement getHomePage_EnqLink()
    {
        return driver.findElement(HomePage_EnqLink);
    }


}
