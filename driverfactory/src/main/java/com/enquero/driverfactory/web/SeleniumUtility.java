package com.enquero.driverfactory.web;




import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
		String actualValue;
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

	 *@NOTE: It will return true If Element Not Present in the DOM else false.
	 */
	public boolean AssertElementNotPresent(String locator,String locatorName) {
		int size;
		switch(locatorName) {
		
		case "id":
			size=driver.findElements(By.id(locator)).size();
			if(size<=0) {
		return true;
		}else {
			return false;
		}case "xpath":
			size=driver.findElements(By.xpath(locator)).size();
			if(size<=0) {
				return true;
		}else {
			return false;
		}case "classname":
			size=driver.findElements(By.className(locator)).size();
			if(size<=0) {
		return true;
		}else {
			return false;
		}case "tagname":
			size=driver.findElements(By.tagName(locator)).size();
			if(size<=0) {
		return true;
		}else {
			return false;
		}case "linktext":
			size=driver.findElements(By.linkText(locator)).size();
			if(size<=0) {
		return true;
		}else {
			return false;
		}
			
		case "partaillinktext":
			size=driver.findElements(By.partialLinkText(locator)).size();
			if(size<=0) {return true;
		}else {
			return false;
		}case "css":
			size=driver.findElements(By.cssSelector(locator)).size();
			if(size<=0) {
		return true;
		}else {
			return false;
		}
		case "name":
			size=driver.findElements(By.name(locator)).size();
			if(size<=0) {
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
	 	 * @param classExpected as the class of the locator.

	 *@NOTE: It will return true if class matches with the locator mentioned.
	 */
	public boolean AssertElementHasClass(String locator,String locatorName,String classExpected) {
		String classActual;
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

	
}
