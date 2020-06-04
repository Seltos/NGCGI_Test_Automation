package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateStory
{
    public WebDriver driver;
    By CreateStory_Button=By.id("createGlobalItemIconButton");
    By CreateStory_Button1=By.xpath("//button/span/span[text()='Create']");
    By CreateStory_Assignee= By.xpath("//*[@id='assignee-field']");
    By CreateStory_IssueType_Input=By.xpath("//*[@id='issuetype-field']");
    By CreateStory_EnqLink=By.xpath("//span[text()='Enquero']");
    By CreateStory_Summary_Input=By.name("summary");
    By CreateStory_Description_textArea=By.xpath("//*[@id='description']");
    By CreateStory_Priority_Input=By.xpath("//*[@id='priority-field']");
    By CreateStory_Env_TextArea=By.xpath("//*[@id='environment']");
    By CreateStory_DueDate_Input=By.xpath("//*[@id='duedate']");//5/May/20
    By CreateStory_Create_Submit=By.xpath("//input[@id='create-issue-submit']");
    By CreateStory_YourWork_Button=By.xpath("//*[@id='helpPanelContainer']/div/div[1]/div[1]/header/nav/div[2]/div[1]/a/span/span");




   public CreateStory(WebDriver driver)
    {
        this.driver=driver;
    }
    public WebElement getCreateStory_Button()
    {
        return driver.findElement(CreateStory_Button);
    }
    public WebElement getCreateStory_Button1()
    {
        return driver.findElement(CreateStory_Button1);
    }
    public WebElement getCreateStory_YourWork_Button()
    {
        return driver.findElement(CreateStory_YourWork_Button);
    }
    public WebElement getCreateStory_IssueType_Input()
    {
        return driver.findElement(CreateStory_IssueType_Input);
    }
    public WebElement getCreateStory_Assignee()
    {
        return driver.findElement(CreateStory_Assignee);
    }
    public WebElement getCreateStory_Summary_Input()
    {
        return driver.findElement(CreateStory_Summary_Input);
    }

    public WebElement getCreateStory_EnqLink()
    {
        return driver.findElement(CreateStory_EnqLink);
    }

    public WebElement getCreateStory_Priority_Input()
    {
        return driver.findElement(CreateStory_Priority_Input);
    }
    public WebElement getCreateStory_Env_TextArea()
    {
        return driver.findElement(CreateStory_Env_TextArea);
    }
    public WebElement getCreateStory_Create_Submit()
    {
        return driver.findElement(CreateStory_Create_Submit);
    }
    public WebElement getCreateStory_DueDate_Input()
    {
        return driver.findElement(CreateStory_DueDate_Input);
    }
    public WebElement getCreateStory_Description_textArea()
    {
        return driver.findElement(CreateStory_Description_textArea);
    }


}
