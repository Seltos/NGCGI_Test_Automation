package HelperUtilities;

        import java.io.FileInputStream;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Properties;

       // import com.sun.deploy.cache.BaseLocalApplicationProperties;
        import org.apache.log4j.Logger;
        import org.apache.poi.ss.usermodel.Cell;
        import org.apache.poi.ss.usermodel.Row;
        import org.apache.poi.ss.usermodel.Sheet;
        import org.apache.poi.ss.usermodel.Workbook;
        import org.apache.poi.xssf.usermodel.XSSFWorkbook;
        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.ui.Select;




public class BDD_Common_Utilities {
    BDD_Common_Utilities appInd;
    /********************************
     * Method Name			: clickObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */

    public void writeResults(String strStatus,String strMessage)
    {
        Logger log=null;

        try
        {
            log=Logger.getLogger("Reporting");
            switch(strStatus.toLowerCase())
            {
                case "pass":log.info(strMessage);
                    break;
                case "fail":log.error(strMessage);
                    break;
                case "exception":log.fatal(strMessage);
                    break;
                case "#":log.info(strMessage);
                    break;
                case "warning":log.warn(strMessage);
                    break;

            }
        } catch(Exception e)
        {
            System.out.println("Exception while executing 'writeResults' method. "+e.getMessage());

        }

    }

    /********************************
     * Method Name			: clickObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean clickObject(WebDriver oDriver, By objBy)
    {
        WebElement oEle = null;
        BDD_Common_Utilities appInd=new BDD_Common_Utilities();
        try {
            oEle = oDriver.findElement(objBy);
            if(oEle.isDisplayed())
            {
                oEle.click();
                appInd.writeResults("Pass","The element '"+String.valueOf(objBy)+"' was clicked successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+String.valueOf(objBy)+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'clickObject' method. "+e.getMessage());
            return false;
        }
    }


    /********************************
     * Method Name			: clickObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean clickObject(WebDriver oDriver, String strObjectName)
    {
        WebElement oEle = null;
        try {
            oEle = appInd.createAndGetObject(oDriver, strObjectName);
            if(oEle.isDisplayed())
            {
                oEle.click();
                appInd.writeResults("Pass","The element '"+strObjectName+"' was clicked successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+strObjectName+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'clickObject' method. "+e.getMessage());
            return false;
        }
    }


    /********************************
     * Method Name			: setObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean setObject(WebDriver oDriver, By objBy, String strData)
    {
        WebElement oEle = null;

        try {
            oEle = oDriver.findElement(objBy);
            if(oEle.isDisplayed())
            {
                oEle.sendKeys(strData);
                appInd.writeResults("Pass","The value '"+strData+"' was entered in the element '"+String.valueOf(objBy)+"' successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+String.valueOf(objBy)+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'setObject' method. "+e.getMessage());
            return false;
        }
    }

    /********************************
     * Method Name			: setObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean setObject(WebDriver oDriver, String strObjectName, String strData)
    {
        WebElement oEle = null;
        try {
            oEle = appInd.createAndGetObject(oDriver, strObjectName);
            if(oEle.isDisplayed())
            {
                oEle.sendKeys(strData);
                appInd.writeResults("Pass","The value '"+strData+"' was entered in the element '"+strObjectName+"' successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+strObjectName+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'setObject' method. "+e.getMessage());
            return false;
        }
    }


    /********************************
     * Method Name			: verifyText
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean verifyText(WebDriver oDriver, By objBy, String strExpected, String strObjType)
    {
        WebElement oEle = null;
        String strActual = null;
        Select oSel = null;
        try {
            oEle = oDriver.findElement(objBy);
            if(oEle.isDisplayed())
            {
                switch(strObjType.toLowerCase())
                {
                    case "text":
                        strActual = oEle.getText();
                        break;
                    case "value":
                        strActual = oEle.getAttribute("value");
                        break;
                    case "list":
                        oSel = new Select(oEle);
                        strActual = oSel.getFirstSelectedOption().getText();
                        break;
                    default:
                        appInd.writeResults("Fail","Invalid object type '"+strObjType+"' was specified");
                        return false;
                }

                if(appInd.compareValue(strActual, strExpected))
                {
                    return true;
                }else{
                    return false;
                }

            }else {
                appInd.writeResults("Fail","The element '"+String.valueOf(objBy)+"' doesn't displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'verifyText' method. "+e.getMessage());
            return false;
        }
    }


    /********************************
     * Method Name			: clearAndSetObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean clearAndSetObject(WebDriver oDriver, By objBy, String strData)
    {
        WebElement oEle = null;
        try {
            oEle = oDriver.findElement(objBy);
            if(oEle.isDisplayed())
            {
                oEle.clear();
                oEle.sendKeys(strData);
                appInd.writeResults("Pass","The value '"+strData+"' was entered in the element '"+String.valueOf(objBy)+"' successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+String.valueOf(objBy)+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'clearAndSetObject' method. "+e.getMessage());
            return false;
        }
    }

    /********************************
     * Method Name			: clearAndSetObject
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean clearAndSetObject(WebDriver oDriver, String strObjectName, String strData)
    {
        WebElement oEle = null;
        try {
            oEle = appInd.createAndGetObject(oDriver, strObjectName);
            if(oEle.isDisplayed())
            {
                oEle.clear();
                oEle.sendKeys(strData);
                appInd.writeResults("Pass","The value '"+strData+"' was entered in the element '"+strObjectName+"' successful");
                return true;
            }else {
                appInd.writeResults("Fail","The element '"+strObjectName+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'clearAndSetObject' method. "+e.getMessage());
            return false;
        }
    }
    /********************************
     * Method Name			: verifyText
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean verifyText(WebDriver oDriver, String strObjectName, String strExpected, String strObjType)
    {
        WebElement oEle = null;
        String strActual = null;
        Select oSel = null;
        try {
            oEle = appInd.createAndGetObject(oDriver, strObjectName);
            if(oEle.isDisplayed())
            {
                switch(strObjType.toLowerCase())
                {
                    case "text":
                        strActual = oEle.getText();
                        break;
                    case "value":
                        strActual = oEle.getAttribute("value");
                        break;
                    case "list":
                        oSel = new Select(oEle);
                        strActual = oSel.getFirstSelectedOption().getText();
                        break;
                    default:
                        appInd.writeResults("Fail","Invalid object type '"+strObjType+"' was specified");
                        return false;
                }

                if(appInd.compareValue(strActual, strExpected))
                {
                    return true;
                }else{
                    return false;
                }

            }else {
                appInd.writeResults("Fail","The element '"+strObjectName+"' doesn't displayed");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'verifyText' method. "+e.getMessage());
            return false;
        }
    }


    /********************************
     * Method Name			: compareValue
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean compareValue(String strActual, String strExpected)
    {
        try {
            if(strActual.equalsIgnoreCase(strExpected))
            {
                appInd.writeResults("Pass","Both actual '"+strActual+"' and Expected '"+strExpected+"' are same");
                return true;
            }else {
                appInd.writeResults("Fail","Both actual '"+strActual+"' and Expected '"+strExpected+"' are NOT same");
                return false;
            }
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'compareValue' method. "+e.getMessage());
            return false;
        }
    }
    /********************************
     * Method Name			: readConfig
     * Purpose				:
     * Author				:Mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public void readConfig()
    {
        FileInputStream	fin = null;
        Properties prop = null;
        try {
            fin = new FileInputStream("path\\Configurartion\\config.properties");
            prop = new Properties();

            prop.load(fin);

            System.setProperty("URL", prop.getProperty("URL"));
            System.setProperty("Browser", prop.getProperty("Browser"));
        }catch(Exception e)
        {
            appInd.writeResults("Fail","Exception while executing 'readConfig' method. "+e.getMessage());
        }finally {
            try {
                fin.close();
                fin = null;
                prop = null;
            }catch(Exception e)
            {
                appInd.writeResults("Fail","Exception while executing 'readConfig' method. "+e.getMessage());
            }
        }
    }

    /********************************
     * Method Name			: verifyObjectExist
     * Purpose				:
     * Author				:
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean verifyObjectExist(WebDriver oDriver, By objBy)
    {
        WebElement oEle = null;
        try {
            oEle = oDriver.findElement(objBy);
            if(oEle.isDisplayed())
            {
                appInd.writeResults("Pass", "The element '"+String.valueOf(objBy)+"' displayed successful");
                return true;
            }else {
                appInd.writeResults("Fail", "The element '"+String.valueOf(objBy)+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            System.out.println("Exception while executing 'writeResult' method. "+e.getMessage());
            return false;
        }finally {
            oEle=null;
        }
    }


    /********************************
     * Method Name			: verifyObjectExist
     * Purpose				:
     * Author				:mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public boolean verifyObjectExist(WebDriver oDriver, String strObjectName)
    {
        WebElement oEle = null;
        try {
            oEle = appInd.createAndGetObject(oDriver, strObjectName);
            if(oEle.isDisplayed())
            {
                appInd.writeResults("Pass", "The element '"+strObjectName+"' displayed successful");
                return true;
            }else {
                appInd.writeResults("Fail", "The element '"+strObjectName+"' was not displayed");
                return false;
            }
        }catch(Exception e)
        {
            System.out.println("Exception while executing 'writeResults' method. "+e.getMessage());
            return false;
        }finally {
            oEle=null;
        }
    }


    /********************************
     * Method Name			: getAndConvertToMap
     * Purpose				:
     * Author				:mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public Map<String, String> getAndConvertToMap()
    {
        FileInputStream fin = null;
        Map<String, String> oMap = null;
        Workbook wb = null;
        Sheet sh = null;
        Row row = null;
        Cell cell1 = null;
        Cell cell2 = null;
        Cell cell3 = null;
        int rows = 0;
        String sKey = null;
        String sVal = null;
        try {
            oMap = new HashMap<String, String>();
            fin = new FileInputStream(System.getProperty("user.dir")+"\\ObjectMapping\\ObjectMapping.xlsx");
            wb = new XSSFWorkbook(fin);
            sh = wb.getSheet("actiTime");
            if(sh==null) return null;
            rows = sh.getPhysicalNumberOfRows();
            for(int r=1;r<rows;r++)
            {
                row = sh.getRow(r);
                cell1 = row.getCell(0);
                sKey = cell1.getStringCellValue();
                cell2 = row.getCell(1);
                cell3 = row.getCell(2);
                sVal = cell2.getStringCellValue()+"#"+cell3.getStringCellValue();
                oMap.put(sKey, sVal);
            }
            return oMap;
        }catch(Exception e)
        {
            System.out.println("Exception while executing 'getAndConvertToMap' method. "+e.getMessage());
            return null;
        }
        finally {
            try {
                fin.close();
                fin = null;
                cell1 = null;
                cell2 = null;
                cell3 = null;
                row= null;
                sh = null;
                wb.close();
                wb = null;
            }catch(Exception e)
            {
                System.out.println("Exception while executing 'getAndConvertToMap' method. "+e.getMessage());
                return null;
            }
        }
    }



    /********************************
     * Method Name			: createAndGetObject
     * Purpose				:
     * Author				:mala
     * Reviewer 			:
     * Date of Creation		:
     * Date of modification :
     * ******************************
     */
    public WebElement createAndGetObject(WebDriver oDriver, String strObjectName)
    {
        WebElement oEle = null;
        String strObjDesc = null;
        try {
            //COMMENT BELOW LINE AFTER ADDING MAP
          //  BaseLocalApplicationProperties oMap;
            //UNCOMMENT BELOW LINE
          //  strObjDesc = oMap.get(strObjectName);
            String objArr[] = strObjDesc.split("#");
            switch(objArr[0].toLowerCase())
            {
                case "id":
                    oEle = oDriver.findElement(By.id(objArr[1]));
                    break;
                case "name":
                    oEle = oDriver.findElement(By.name(objArr[1]));
                    break;
                case "xpath":
                    oEle = oDriver.findElement(By.xpath(objArr[1]));
                    break;
                case "cssselector":
                    oEle = oDriver.findElement(By.cssSelector(objArr[1]));
                    break;
                case "linktext":
                    oEle = oDriver.findElement(By.linkText(objArr[1]));
                    break;
                default:
                    appInd.writeResults("Fail", "Invalid locator name '"+objArr[0]+"'");
                    return null;
            }
            return oEle;
        }catch(Exception e)
        {
            System.out.println("Exception while executing 'createAndGetObject' method. "+e.getMessage());
            return null;
        }
    }








}