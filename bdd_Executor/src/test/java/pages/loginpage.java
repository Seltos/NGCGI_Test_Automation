package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class loginpage
{
    public WebDriver driver;
    By Login_UserName=By.xpath("//input[@autocomplete='username']");
    By Login_PassWord= By.xpath("//input[@id='password']");
    By Login_Submit=By.xpath("//button[@id='login-submit']");

    By Logout_Profile = By.xpath("//button//span[contains(@class,'styledCache')]");
    By Logout = By.xpath("//a[contains(@data-testid,'log-out')]");
    By Logout_Submit=By.xpath("//input[@id='logout-submit']");

    public WebElement getLogout_Profile(){return driver.findElement(Logout_Profile);}
    public WebElement getLogout(){return driver.findElement(Logout);}
    public WebElement getLogout_Submit(){return driver.findElement(Logout_Submit);}

   public loginpage(WebDriver driver)
    {
        this.driver=driver;
    }
    public WebElement getLogin_UserName()
    {
        return driver.findElement(Login_UserName);
    }

    public WebElement getLogin_PassWord()
    {
        return driver.findElement(Login_PassWord);
    }
    public WebElement getLogin_Submit()
    {
        return driver.findElement(Login_Submit);
    }


}
