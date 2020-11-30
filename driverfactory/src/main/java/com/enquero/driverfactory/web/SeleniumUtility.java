package com.enquero.driverfactory.web;




import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


/**
 * @author GauravMahajan
 *
 */
public class SeleniumUtility {
public WebDriver driver;
	public SeleniumUtility(WebDriver driver) {
		
		this.driver=driver;
		
	}
	/**
	 * @param CookieName as the Name of the Cookie
	 * @param CookieValue as the Value of the Cookie
	 */

	public void AddCookie(String CookieName,String CookieValue)
	{
		org.openqa.selenium.Cookie cookie=new org.openqa.selenium.Cookie(CookieName, CookieValue);
		driver.manage().addCookie(cookie);
		}
	
	/**
	 * @param locator as the value of the locator.e.g:"value of xpath or id or linktext or tagname or partial linktext or name or classname.
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 * @param attributeName as the name of the attribute.
	 * @param attributeValue as the value of the attribute.e.g:Behind Equals and EqualsIgnoreCase methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @param valueContains as the value of the attribute.e.g:Behind contains methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @param valueMatches as the value of the attribute.e.g:Behind AssertSame methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @NOTE: At One Time You can pass any one value in last three parameters for comparison.You can either pass  value in attributeValue or valueContains or valueMatches
	 * @param caseInsensitive as the true or false.If it is true,Then EqualsIgnoreCase will be called otherwise If It is equals to false then Equals Method will be called.
	 *
	 */

	public void AssertAttributeValue(String locator,String locatorName,String attributeName,String attributeValue,String valueContains,String valueMatches,String caseInsensitive)
	{
		String actualValue="";
			switch(caseInsensitive) {
			
			
			case "true":
			switch(locatorName) {
			case "id":
				actualValue=driver.findElement(By.id(locator)).getAttribute(attributeName);
if(attributeValue!="")	{

	Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
	break;
}else if(valueContains!=""){
	
	if(actualValue.contains(valueContains)) {
		
	
	Assert.assertTrue(true, "Value is Conatined");
	break;
}else {
	Assert.assertTrue(false, "Value is not Conatined");
	break;

}
	


}
	else if(valueMatches!=""){
		Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
break;
	}
	case "xpath":
		actualValue=driver.findElement(By.xpath(locator)).getAttribute(attributeName);
		if(attributeValue!="")	{
			Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
			break;
		}else if(valueContains!=""){
			
			if(actualValue.contains(valueContains)) {
				
			
			Assert.assertTrue(true, "Value is Conatined");
			break;
		}
			else {
				Assert.assertTrue(false, "Value is not Conatined");
				break;

			}
}
			else if(valueMatches!=""){
				Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
		break;
			}
			


		case "name":
			actualValue=driver.findElement(By.name(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}else {
				Assert.assertTrue(false, "Value is not Conatined");
				break;

			}
}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
		case "css":
			actualValue=driver.findElement(By.cssSelector(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
				
		case "classname":
			actualValue=driver.findElement(By.className(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
		case "tagname":
			actualValue=driver.findElement(By.tagName(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}

		case "linktext":
			actualValue=driver.findElement(By.linkText(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
break;
				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}

		case "partialLinkText":
			actualValue=driver.findElement(By.partialLinkText(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(attributeValue), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
break;
				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}


	}
			
			
			
			
	case "false":
				switch(locatorName) {
				case "id":
					actualValue=driver.findElement(By.id(locator)).getAttribute(attributeName);
	if(attributeValue!="")	{

		Assert.assertEquals(actualValue, attributeValue);
		break;
	}else if(valueContains!=""){
		
		if(actualValue.contains(valueContains)) {
			
		
		Assert.assertTrue(true, "Value is Conatined");
		break;
	}else {
		Assert.assertTrue(false, "Value is not Conatined");
		break;

	}
		


	}
		else if(valueMatches!=""){
			Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
	break;
		}
		case "xpath":
			actualValue=driver.findElement(By.xpath(locator)).getAttribute(attributeName);
			if(attributeValue!="")	{
				Assert.assertEquals(actualValue, attributeValue);
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
				


			case "name":
				actualValue=driver.findElement(By.name(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
			case "css":
				actualValue=driver.findElement(By.cssSelector(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
					
			case "classname":
				actualValue=driver.findElement(By.className(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
			case "tagname":
				actualValue=driver.findElement(By.tagName(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}

			case "linktext":
				actualValue=driver.findElement(By.linkText(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
	break;
					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}

			case "partialLinkText":
				actualValue=driver.findElement(By.partialLinkText(locator)).getAttribute(attributeName);
				if(attributeValue!="")	{
					Assert.assertEquals(actualValue, attributeValue);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
	break;
					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}


		
				}}
			
			
			
			
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is enabled else false.
	 */
	public boolean AssertElementEnabled(String locator,String locatorName) {
		boolean isDisabled = false;
		switch(locatorName) {
		
		case "id":
			isDisabled=driver.findElement(By.id(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}case "xpath":
			isDisabled=driver.findElement(By.xpath(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}case "classname":
			isDisabled=driver.findElement(By.className(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}case "tagname":
			isDisabled=driver.findElement(By.tagName(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}case "linktext":
			isDisabled=driver.findElement(By.linkText(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isDisabled=driver.findElement(By.partialLinkText(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}case "css":
			isDisabled=driver.findElement(By.cssSelector(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}
		case "name":
			isDisabled=driver.findElement(By.name(locator)).isEnabled();
			if(isDisabled==true) {
		return true;
		}else {
			return false;
		}
	}
		return isDisabled;
	}
	
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".

	 *@NOTE: It will return true If Element is Present in the DOM else false.
	 */
	public boolean AssertElementPresent(String locator,String locatorName) {
		int size=0;
		boolean element=false;
		switch(locatorName) {
		
		case "id":
			size=driver.findElements(By.id(locator)).size();
			if(size>0) {
				element=true;
		return element;
		}else {
			element=false;

			return element;
		}case "xpath":
			size=driver.findElements(By.xpath(locator)).size();
			if(size>0) {
				element=true;
				return element;

		}else {
			element=false;
			return element;		}case "classname":
			size=driver.findElements(By.className(locator)).size();
			if(size>0) {
				element=true;
				return element;
		}else {
			element=false;
			return element;
		}case "tagname":
			size=driver.findElements(By.tagName(locator)).size();
			if(size>0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}case "linktext":
			size=driver.findElements(By.linkText(locator)).size();
			if(size>0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
			
		case "partaillinktext":
			size=driver.findElements(By.partialLinkText(locator)).size();
			if(size>0) {
element=true;
			return element;
		}else {
			element=false;
			return element;		}case "css":
			size=driver.findElements(By.cssSelector(locator)).size();
			if(size>0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
		case "name":
			size=driver.findElements(By.name(locator)).size();
			if(size>0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
	}
		return element;	}
	
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is  readonly else false.
	 */
	public boolean AssertElementReadOnly(String locator,String locatorName) {
		boolean isReadOnly = false;
		switch(locatorName) {
		
		case "id":
			isReadOnly=driver.findElement(By.id(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}case "xpath":
			isReadOnly=driver.findElement(By.xpath(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}case "classname":
			isReadOnly=driver.findElement(By.className(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}case "tagname":
			isReadOnly=driver.findElement(By.tagName(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}case "linktext":
			isReadOnly=driver.findElement(By.linkText(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isReadOnly=driver.findElement(By.partialLinkText(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}case "css":
			isReadOnly=driver.findElement(By.cssSelector(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}
		case "name":
			isReadOnly=driver.findElement(By.name(locator)).isEnabled();
			if(isReadOnly==true) {
		return true;
		}else {
			return false;
		}
	}
		return isReadOnly;
	}
	
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 	 * @param classExpected as the class of the locator.

	 *@NOTE: It will return true if class matches with the locator mentioned.
	 */
	public boolean AssertElementHasClass(String locator,String locatorName,String classExpected) {
		String classActual="";
		switch(locatorName) {
		
		case "id":
			classActual=driver.findElement(By.id(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}case "xpath":
			classActual=driver.findElement(By.xpath(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
				return true;
		}else {
			return false;
		}case "classname":
			classActual=driver.findElement(By.className(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}case "tagname":
			classActual=driver.findElement(By.tagName(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}case "linktext":
			classActual=driver.findElement(By.linkText(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			classActual=driver.findElement(By.partialLinkText(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {		return true;
		}else {
			return false;
		}case "css":
			classActual=driver.findElement(By.cssSelector(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}
		case "name":
			classActual=driver.findElement(By.name(locator)).getAttribute("class");
			if(classActual.equals(classExpected)) {
		return true;
		}else {
			return false;
		}
	}
		return false;
	}

	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is disabled else false.
	 */
	public boolean AssertElementDisabled(String locator,String locatorName) {
		boolean isDisabled = false;
		switch(locatorName) {
		
		case "id":
			isDisabled=driver.findElement(By.id(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}case "xpath":
			isDisabled=driver.findElement(By.xpath(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}case "classname":
			isDisabled=driver.findElement(By.className(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}case "tagname":
			isDisabled=driver.findElement(By.tagName(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}case "linktext":
			isDisabled=driver.findElement(By.linkText(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isDisabled=driver.findElement(By.partialLinkText(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}case "css":
			isDisabled=driver.findElement(By.cssSelector(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}
		case "name":
			isDisabled=driver.findElement(By.name(locator)).isEnabled();
			if(isDisabled==false) {
		return true;
		}else {
			return false;
		}
	}
		return isDisabled;
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is not visible else false.
	 */
	public boolean AssertElementNotVisible(String locator,String locatorName) {
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		boolean visible=false;
		switch(locatorName) {
		
		case "id":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locator))); 

			if(visible==true) {
		return true;
		}else {
			return false;
		}case "xpath":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator))); 

			if(visible==true) {

		return true;
		}else {
			return false;
		}case "classname":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(locator))); 

			if(visible==true) {
		return true;
		}else {
			return false;
		}case "tagname":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName(locator))); 

			if(visible==true) {

		return true;
		}else {
			return false;
		}case "linktext":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locator))); 

			if(visible==true) {

		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText(locator))); 

			if(visible==true) {

		return true;
		}else {
			return false;
		}case "css":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator))); 

			if(visible==true) {
		return true;
		}else {
			return false;
		}
		case "name":
			visible=wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(locator))); 

			if(visible==true) {

		return true;
		}else {
			return false;
		}
	}
		return visible;
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 * @param optionValue is given then it will deselect value from dropdown using Value
	 * @param optionText is given then From DropDown,it will deselect value on basis of visibletext method
	 * @param optionNumber is given then From DropDown,it will deselect value on basis of selectbyindex method

	 *@NOTE: Only One value from optionValue,optionText,optionNumber can be given to deselect the value.
	 */
	public void DeselectListOption(String locator,String locatorName,String optionValue,String optionText,String optionNumber) {
		switch(locatorName) {
		
		case "id":
			if(optionValue!="") {
			Select select=new Select(driver.findElement(By.id(locator)));
select.deselectByValue(optionValue);

}else if(optionText!="") {
	Select select=new Select(driver.findElement(By.id(locator)));
	select.deselectByVisibleText(optionText);

}else if(optionNumber!="") {
	Select select=new Select(driver.findElement(By.id(locator)));
	select.deselectByIndex(Integer.parseInt(optionNumber));

}
			case "xpath":
				if(optionValue!="") {
					Select select=new Select(driver.findElement(By.xpath(locator)));
		select.deselectByValue(optionValue);

		}else if(optionText!="") {
			Select select=new Select(driver.findElement(By.xpath(locator)));
			select.deselectByVisibleText(optionText);

		}else if(optionNumber!="") {
			Select select=new Select(driver.findElement(By.xpath(locator)));
			select.deselectByIndex(Integer.parseInt(optionNumber));

		}
case "classname":
	if(optionValue!="") {
		Select select=new Select(driver.findElement(By.className(locator)));
select.deselectByValue(optionValue);

}else if(optionText!="") {
Select select=new Select(driver.findElement(By.className(locator)));
select.deselectByVisibleText(optionText);

}else if(optionNumber!="") {
Select select=new Select(driver.findElement(By.className(locator)));
select.deselectByIndex(Integer.parseInt(optionNumber));

}
case "tagname":
			if(optionValue!="") {
				Select select=new Select(driver.findElement(By.tagName(locator)));
		select.deselectByValue(optionValue);

		}else if(optionText!="") {
		Select select=new Select(driver.findElement(By.tagName(locator)));
		select.deselectByVisibleText(optionText);

		}else if(optionNumber!="") {
		Select select=new Select(driver.findElement(By.tagName(locator)));
		select.deselectByIndex(Integer.parseInt(optionNumber));

		}	
		case "partaillinktext":
			if(optionValue!="") {
				Select select=new Select(driver.findElement(By.partialLinkText(locator)));
		select.deselectByValue(optionValue);

		}else if(optionText!="") {
		Select select=new Select(driver.findElement(By.partialLinkText(locator)));
		select.deselectByVisibleText(optionText);

		}else if(optionNumber!="") {
		Select select=new Select(driver.findElement(By.partialLinkText(locator)));
		select.deselectByIndex(Integer.parseInt(optionNumber));

		}	
case "css":
	if(optionValue!="") {
		Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.deselectByValue(optionValue);

}else if(optionText!="") {
Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.deselectByVisibleText(optionText);

}else if(optionNumber!="") {
Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.deselectByIndex(Integer.parseInt(optionNumber));

}	

		case "name":
			if(optionValue!="") {
				Select select=new Select(driver.findElement(By.name(locator)));
		select.deselectByValue(optionValue);

		}else if(optionText!="") {
		Select select=new Select(driver.findElement(By.name(locator)));
		select.deselectByVisibleText(optionText);

		}else if(optionNumber!="") {
		Select select=new Select(driver.findElement(By.name(locator)));
		select.deselectByIndex(Integer.parseInt(optionNumber));

		}}
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".

	 *@NOTE: It will return true If Element Not Present in the DOM else false.
	 */
	public boolean AssertElementNotPresent(String locator,String locatorName) {
		int size=0;
		boolean element=false;
		switch(locatorName) {
		
		case "id":
			size=driver.findElements(By.id(locator)).size();
			if(size<=0) {
				element=true;
		return element;
		}else {
			element=false;

			return element;
		}case "xpath":
			size=driver.findElements(By.xpath(locator)).size();
			if(size<=0) {
				element=true;
				return element;

		}else {
			element=false;
			return element;		}case "classname":
			size=driver.findElements(By.className(locator)).size();
			if(size<=0) {
				element=true;
				return element;
		}else {
			element=false;
			return element;
		}case "tagname":
			size=driver.findElements(By.tagName(locator)).size();
			if(size<=0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}case "linktext":
			size=driver.findElements(By.linkText(locator)).size();
			if(size<=0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
			
		case "partaillinktext":
			size=driver.findElements(By.partialLinkText(locator)).size();
			if(size<=0) {element=true;
			return element;
		}else {
			element=false;
			return element;		}case "css":
			size=driver.findElements(By.cssSelector(locator)).size();
			if(size<=0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
		case "name":
			size=driver.findElements(By.name(locator)).size();
			if(size<=0) {
				element=true;
				return element;		}else {
					element=false;
					return element;		}
	}
		return element;	}
	
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is not readonly else false.
	 */
	public boolean AssertElementNotReadOnly(String locator,String locatorName) {
		boolean isReadOnly = false;
		switch(locatorName) {
		
		case "id":
			isReadOnly=driver.findElement(By.id(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}case "xpath":
			isReadOnly=driver.findElement(By.xpath(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}case "classname":
			isReadOnly=driver.findElement(By.className(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}case "tagname":
			isReadOnly=driver.findElement(By.tagName(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}case "linktext":
			isReadOnly=driver.findElement(By.linkText(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isReadOnly=driver.findElement(By.partialLinkText(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}case "css":
			isReadOnly=driver.findElement(By.cssSelector(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}
		case "name":
			isReadOnly=driver.findElement(By.name(locator)).isEnabled();
			if(isReadOnly==false) {
		return true;
		}else {
			return false;
		}
	}
		return isReadOnly;
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is not selected else false.
	 */
	public boolean AssertElementNotSelected(String locator,String locatorName) {
		boolean isSelected = false;
		switch(locatorName) {
		
		case "id":
			isSelected=driver.findElement(By.id(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "xpath":
			isSelected=driver.findElement(By.xpath(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "classname":
			isSelected=driver.findElement(By.className(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "tagname":
			isSelected=driver.findElement(By.tagName(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "linktext":
			isSelected=driver.findElement(By.linkText(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isSelected=driver.findElement(By.partialLinkText(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "css":
			isSelected=driver.findElement(By.cssSelector(locator)).isSelected();
			if(isSelected==false) {		return true;
		}else {
			return false;
		}
		case "name":
			isSelected=driver.findElement(By.name(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}
	}
		return isSelected;
	}
	
	/**
	 * @param locator as the value of the locator.e.g:"value of xpath or id or linktext or tagname or partial linktext or name or classname.
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 * @param text as the value of the element.e.g:Behind Equals and EqualsIgnoreCase methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @param valueContains as the value of the attribute.e.g:Behind contains methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @param valueMatches as the value of the attribute.e.g:Behind AssertSame methods will be called to compare values,one which you will pass here and other retrieved using attributeName value which you passed.
	 * @NOTE: At One Time You can pass any one value in last three parameters for comparison.You can either pass  value in text or valueContains or valueMatches
	 * @param caseInsensitive as the true or false.If it is true,Then EqualsIgnoreCase will be called otherwise If It is equals to false then Equals Method will be called.
	 *
	 */

	public void AssertElementText(String locator,String locatorName,String text,String valueContains,String valueMatches,String caseInsensitive)
	{
		String actualValue="";
			switch(caseInsensitive) {
			
			
			case "true":
			switch(locatorName) {
			case "id":
				actualValue=driver.findElement(By.id(locator)).getText();
if(actualValue!="")	{

	Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
	break;
}else if(valueContains!=""){
	
	if(actualValue.contains(valueContains)) {
		
	
	Assert.assertTrue(true, "Value is Conatined");
	break;
}else {
	Assert.assertTrue(false, "Value is not Conatined");
	break;

}
	


}
	else if(valueMatches!=""){
		Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
break;
	}
	case "xpath":
		actualValue=driver.findElement(By.xpath(locator)).getText();
		if(actualValue!="")	{
			Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
			break;
		}else if(valueContains!=""){
			
			if(actualValue.contains(valueContains)) {
				
			
			Assert.assertTrue(true, "Value is Conatined");
			break;
		}
			else {
				Assert.assertTrue(false, "Value is not Conatined");
				break;

			}
}
			else if(valueMatches!=""){
				Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
		break;
			}
			


		case "name":
			actualValue=driver.findElement(By.name(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}else  {
				Assert.assertTrue(false, "Value is not Conatined");
				break;

			}
}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
		case "css":
			actualValue=driver.findElement(By.cssSelector(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
				
		case "classname":
			actualValue=driver.findElement(By.className(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
		case "tagname":
			actualValue=driver.findElement(By.tagName(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}

		case "linktext":
			actualValue=driver.findElement(By.linkText(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
break;
				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}

		case "partialLinkText":
			actualValue=driver.findElement(By.partialLinkText(locator)).getText();
			if(actualValue!="")	{
				Assert.assertTrue(actualValue.equalsIgnoreCase(text), "Value are not Caseinsensitive");
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
break;
				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}


	}
			
			
			
			
	case "false":
				switch(locatorName) {
				case "id":
					actualValue=driver.findElement(By.id(locator)).getText();
	if(actualValue!="")	{

		Assert.assertEquals(actualValue, text);
		break;
	}else if(valueContains!=""){
		
		if(actualValue.contains(valueContains)) {
			
		
		Assert.assertTrue(true, "Value is Conatined");
		break;
	}else {
		Assert.assertTrue(false, "Value is not Conatined");
		break;

	}
		


	}
		else if(valueMatches!=""){
			Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
	break;
		}
		case "xpath":
			actualValue=driver.findElement(By.xpath(locator)).getText();
			if(actualValue!="")	{
				Assert.assertEquals(actualValue, text);
				break;
			}else if(valueContains!=""){
				
				if(actualValue.contains(valueContains)) {
					
				
				Assert.assertTrue(true, "Value is Conatined");
				break;
			}
				else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
				else if(valueMatches!=""){
					Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
			break;
				}
				


			case "name":
				actualValue=driver.findElement(By.name(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}else {
					Assert.assertTrue(false, "Value is not Conatined");
					break;

				}
	}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
			case "css":
				actualValue=driver.findElement(By.cssSelector(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
					
			case "classname":
				actualValue=driver.findElement(By.className(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}
			case "tagname":
				actualValue=driver.findElement(By.tagName(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
						break;

					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}

			case "linktext":
				actualValue=driver.findElement(By.linkText(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
	break;
					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}

			case "partialLinkText":
				actualValue=driver.findElement(By.partialLinkText(locator)).getText();
				if(actualValue!="")	{
					Assert.assertEquals(actualValue, text);
					break;
				}else if(valueContains!=""){
					
					if(actualValue.contains(valueContains)) {
						
					
					Assert.assertTrue(true, "Value is Conatined");
					break;
				}
					else {
						Assert.assertTrue(false, "Value is not Conatined");
	break;
					}
		}
					else if(valueMatches!=""){
						Assert.assertSame(actualValue, valueMatches, "Value is not Matched");
				break;
					}


		
				}}
			
			
			
			
	}
	
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is  visible else false.
	 */
	public boolean AssertElementVisible(String locator,String locatorName) {
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		WebElement visible;
		boolean isVisible=false;
		switch(locatorName) {
		
		case "id":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator))); 

			if(visible!=null) {
				isVisible=true;
		return isVisible;
		}else {
			isVisible=false;
			return isVisible;		}case "xpath":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator))); 

			if(visible!=null) {

				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}case "classname":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locator))); 

			if(visible!=null) {
				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}case "tagname":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locator))); 

			if(visible!=null) {

				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}case "linktext":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator))); 

			if(visible!=null) {

				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}
			
		case "partaillinktext":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(locator))); 

			if(visible!=null) {

				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}case "css":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator))); 

			if(visible!=null) {
				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}
		case "name":
			visible=wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locator))); 

			if(visible!=null) {

				isVisible=true;
				return isVisible;		}else {
					isVisible=false;
					return isVisible;		}
	}
		return isVisible;	
		
	}
	/**
	 * @param locator as the Value of the locator like xpath value or id value etc
	 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
	 
	 *@NOTE: It will return true if element is  selected else false.
	 */
	public boolean AssertElementSelected(String locator,String locatorName) {
		boolean isSelected = false;
		switch(locatorName) {
		
		case "id":
			isSelected=driver.findElement(By.id(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}case "xpath":
			isSelected=driver.findElement(By.xpath(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}case "classname":
			isSelected=driver.findElement(By.className(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}case "tagname":
			isSelected=driver.findElement(By.tagName(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}case "linktext":
			isSelected=driver.findElement(By.linkText(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			isSelected=driver.findElement(By.partialLinkText(locator)).isSelected();
			if(isSelected==false) {
		return true;
		}else {
			return false;
		}case "css":
			isSelected=driver.findElement(By.cssSelector(locator)).isSelected();
			if(isSelected==true) {
return true;
		}else {
			return false;
		}
		case "name":
			isSelected=driver.findElement(By.name(locator)).isSelected();
			if(isSelected==true) {
		return true;
		}else {
			return false;
		}
	}
		return isSelected;
	}
	/**
	 * @param title as the title of the Page
	 * @param titleContains as the Title of the Page which can be contained on the page
	 *@NOTE: At One Time,Only One value should be passed in the method parameters e.g You can pass value either in title ot titleContains.
	 */
public boolean AssertPageTitle(String title,String titleContains) {
	boolean titleActual=false;
	String string;
if(title!="") {
	string=driver.getTitle();
	Assert.assertEquals(string, title, "Titles using title field are not matching");
	titleActual=true;
	return titleActual;

}else if(titleContains!="") {
	string=driver.getTitle();
	if(string.contains(titleContains)) {
		titleActual=true;
		return titleActual;
	}else {
		titleActual=false;

		return titleActual;
	}


}
return titleActual;
}

/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
 
 *@NOTE: It will clear the contents of the inputbox.please pass only inputbox as value.
 */
public void ClearContent(String locator,String locatorName) {
	switch(locatorName) {
	
	case "id":
		driver.findElement(By.id(locator)).clear();
		case "xpath":
		driver.findElement(By.xpath(locator)).clear();
		case "classname":
		driver.findElement(By.className(locator)).clear();
		case "tagname":
		driver.findElement(By.tagName(locator)).clear();
		case "linktext":
		driver.findElement(By.linkText(locator)).clear();
		
		
	case "partaillinktext":
		driver.findElement(By.partialLinkText(locator)).clear();
		case "css":
		driver.findElement(By.cssSelector(locator)).clear();
	case "name":
		driver.findElement(By.name(locator)).clear();
		
}
}
/**
 *@NOTE: quit or close the driver
 */

public void CloseBrowser()
{
	driver.quit();
	}

/**
 *@NOTE:  close the current tab and then switch to default content or browser window
 */

public void CloseWindow()
{
	
	
	
	
	 Actions actionObj = new Actions(driver);
	    actionObj.keyDown(Keys.CONTROL)
	             .sendKeys(Keys.chord("w"))
	             .keyUp(Keys.CONTROL)
	             .perform();

	    driver.switchTo().defaultContent();	


}
/**
 * @param CookieName as the Name of the Cookie
 * @NOTE delete the cookie.
 */

public void DeleteCookie(String CookieName)
{
	driver.manage().deleteCookieNamed(CookieName);
	}
/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
 * @param optionValue is given then it will select value from dropdown using Value
 * @param optionText is given then From DropDown,it will select value on basis of visibletext method
 * @param optionNumber is given then From DropDown,it will select value on basis of selectbyindex method

 *@NOTE: Only One value from optionValue,optionText,optionNumber can be given to select the value.
 */
public void AssertListOption(String locator,String locatorName,String optionValue,String optionText,String optionNumber) {
	switch(locatorName) {
	
	case "id":
		if(optionValue!="") {
		Select select=new Select(driver.findElement(By.id(locator)));
select.selectByValue(optionValue);

}else if(optionText!="") {
Select select=new Select(driver.findElement(By.id(locator)));
select.selectByVisibleText(optionText);

}else if(optionNumber!="") {
Select select=new Select(driver.findElement(By.id(locator)));
select.selectByIndex(Integer.parseInt(optionNumber));

}
		case "xpath":
			if(optionValue!="") {
				Select select=new Select(driver.findElement(By.xpath(locator)));
	select.selectByValue(optionValue);

	}else if(optionText!="") {
		Select select=new Select(driver.findElement(By.xpath(locator)));
		select.selectByVisibleText(optionText);

	}else if(optionNumber!="") {
		Select select=new Select(driver.findElement(By.xpath(locator)));
		select.selectByIndex(Integer.parseInt(optionNumber));

	}
case "classname":
if(optionValue!="") {
	Select select=new Select(driver.findElement(By.className(locator)));
select.selectByValue(optionValue);

}else if(optionText!="") {
Select select=new Select(driver.findElement(By.className(locator)));
select.selectByVisibleText(optionText);

}else if(optionNumber!="") {
Select select=new Select(driver.findElement(By.className(locator)));
select.selectByIndex(Integer.parseInt(optionNumber));

}
case "tagname":
		if(optionValue!="") {
			Select select=new Select(driver.findElement(By.tagName(locator)));
	select.selectByValue(optionValue);

	}else if(optionText!="") {
	Select select=new Select(driver.findElement(By.tagName(locator)));
	select.selectByVisibleText(optionText);

	}else if(optionNumber!="") {
	Select select=new Select(driver.findElement(By.tagName(locator)));
	select.selectByIndex(Integer.parseInt(optionNumber));

	}	
	case "partaillinktext":
		if(optionValue!="") {
			Select select=new Select(driver.findElement(By.partialLinkText(locator)));
	select.selectByValue(optionValue);

	}else if(optionText!="") {
	Select select=new Select(driver.findElement(By.partialLinkText(locator)));
	select.selectByVisibleText(optionText);

	}else if(optionNumber!="") {
	Select select=new Select(driver.findElement(By.partialLinkText(locator)));
	select.selectByIndex(Integer.parseInt(optionNumber));

	}	
case "css":
if(optionValue!="") {
	Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.selectByValue(optionValue);

}else if(optionText!="") {
Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.selectByVisibleText(optionText);

}else if(optionNumber!="") {
Select select=new Select(driver.findElement(By.cssSelector(locator)));
select.selectByIndex(Integer.parseInt(optionNumber));

}	

	case "name":
		if(optionValue!="") {
			Select select=new Select(driver.findElement(By.name(locator)));
	select.selectByValue(optionValue);

	}else if(optionText!="") {
	Select select=new Select(driver.findElement(By.name(locator)));
	select.selectByVisibleText(optionText);

	}else if(optionNumber!="") {
	Select select=new Select(driver.findElement(By.name(locator)));
	select.selectByIndex(Integer.parseInt(optionNumber));

	}}
}
/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
 * @param property as the name of the css property..

 *@NOTE: It will return value of Css Property
 */
public String ReadCssProperty(String locator,String locatorName,String property) {
	String element="";
	switch(locatorName) {
	
	case "id":
		element=driver.findElement(By.id(locator)).getCssValue(property);
		return element;
		
	case "xpath":
		element=driver.findElement(By.xpath(locator)).getCssValue(property);
		return element;
case "classname":
	element=driver.findElement(By.className(locator)).getCssValue(property);
	return element;
case "tagname":
	element=driver.findElement(By.tagName(locator)).getCssValue(property);
	return element;
case "linktext":
	element=driver.findElement(By.linkText(locator)).getCssValue(property);
	return element;
		
	case "partaillinktext":
		element=driver.findElement(By.partialLinkText(locator)).getCssValue(property);
		return element;
		case "css":
			element=driver.findElement(By.cssSelector(locator)).getCssValue(property);
			return element;
	case "name":
		element=driver.findElement(By.name(locator)).getCssValue(property);
		return element;
}
	return element;	}

/**
 * @NOTE Go to the previous page.
 */

public void NavigateBack()
{
	driver.navigate().back();
	}


/**
 * @NOTE Go to the forward page.
 */

public void NavigateForward()
{
	driver.navigate().forward();
	}

/**
 * @param url navigate to URL
 * @NOTE Go to WebApp
 */

public void NavigateTo(String url)
{
	driver.navigate().to(url);
	}

/**
 * @param CookieName as the Name of the Cookie
 * @NOTE: Returns CookieValue
 */

public String ReadCookie(String CookieName)
{
	Cookie cookie=driver.manage().getCookieNamed(CookieName);
	return cookie.getValue();

}

/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".

 *@NOTE: It will return true If Element Not Present in the DOM else false.
 */
public List<WebElement> GetElements(String locator,String locatorName) {
	List<WebElement> element=new ArrayList<WebElement>();
	switch(locatorName) {
	
	case "id":
		element=driver.findElements(By.id(locator));
		return element;
		
	case "xpath":
		element=driver.findElements(By.xpath(locator));
		return element;
case "classname":
	element=driver.findElements(By.className(locator));
	return element;
case "tagname":
	element=driver.findElements(By.tagName(locator));
	return element;
case "linktext":
	element=driver.findElements(By.linkText(locator));
	return element;
		
	case "partaillinktext":
		element=driver.findElements(By.partialLinkText(locator));
		return element;
		case "css":
			element=driver.findElements(By.cssSelector(locator));
			return element;
	case "name":
		element=driver.findElements(By.name(locator));
		return element;
}
	return element;	}


/**
 * @NOTE Returns the Current URL
 */

public String ReadCurrentUrl()
{
	return driver.getCurrentUrl();
	}

/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".
 * @param property as the name of the  attribute.

 *@NOTE: It will return value of Attribute
 */
public String ReadElementAttribute(String locator,String locatorName,String property) {
	String element="";
	switch(locatorName) {
	
	case "id":
		element=driver.findElement(By.id(locator)).getAttribute(property);
		return element;
		
	case "xpath":
		element=driver.findElement(By.xpath(locator)).getAttribute(property);
		return element;
case "classname":
	element=driver.findElement(By.className(locator)).getAttribute(property);
	return element;
case "tagname":
	element=driver.findElement(By.tagName(locator)).getAttribute(property);
	return element;
case "linktext":
	element=driver.findElement(By.linkText(locator)).getAttribute(property);
	return element;
		
	case "partaillinktext":
		element=driver.findElement(By.partialLinkText(locator)).getAttribute(property);
		return element;
		case "css":
			element=driver.findElement(By.cssSelector(locator)).getAttribute(property);
			return element;
	case "name":
		element=driver.findElement(By.name(locator)).getAttribute(property);
		return element;
}
	return element;	}
/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".

 *@NOTE: It will return count of the no of the elements present.
 */
public int ReadElementCount(String locator,String locatorName) {
	int size=0;
	switch(locatorName) {
	
	case "id":
		size=driver.findElements(By.id(locator)).size();
		return size;
		case "xpath":
		size=driver.findElements(By.xpath(locator)).size();
		return size;
case "classname":
	return size;
case "tagname":
		size=driver.findElements(By.tagName(locator)).size();
		return size;
case "linktext":
		size=driver.findElements(By.linkText(locator)).size();
		return size;

		
	case "partaillinktext":
		size=driver.findElements(By.partialLinkText(locator)).size();
		return size;
case "css":
		size=driver.findElements(By.cssSelector(locator)).size();
		return size;
	case "name":
		size=driver.findElements(By.name(locator)).size();
		return size;

}
	return size;	}


/**
 * @param locator as the Value of the locator like xpath value or id value etc
 * @param locatorName as the name of the locator.e.g:"xpath" or "id" or "linktext" or "tagname" or "partiallinktext" or "name" or "classname".

 *@NOTE: Reads the visible (not hidden by CSS) inner text of this element, including sub-elements, without any leading or trailing whitespace.
 */
public String ReadElementText(String locator,String locatorName) {
	String element="";
	switch(locatorName) {
	
	case "id":
		element=driver.findElement(By.id(locator)).getText();
		return element;
	case "xpath":
		element=driver.findElement(By.xpath(locator)).getText();
		return element;
case "classname":
	element=driver.findElement(By.className(locator)).getText();
	return element;
case "tagname":
	element=driver.findElement(By.tagName(locator)).getText();
	return element;
case "linktext":
	element=driver.findElement(By.linkText(locator)).getText();
	return element;
		
	case "partaillinktext":
		element=driver.findElement(By.partialLinkText(locator)).getText();
		return element;
		case "css":
			element=driver.findElement(By.cssSelector(locator)).getText();
			return element;
	case "name":
		element=driver.findElement(By.name(locator)).getText();
		return element;
}
	return element;	}
/**
 * @NOTE: Returns Page Source
 */

public String ReadPageSource()
{
	return driver.getPageSource();

}

}
