package com.enquero.reporter;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.enquero.TestListener.AllureExtentTestNGListener;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestReporter {
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extentReport;
    public static ExtentTest extentTest;
    private static String reportFileName = "Extent_TestReport" + ".html";
    private static String fileSeperator = System.getProperty("file.separator");
    private static String reportFilepath = System.getProperty("user.dir") + fileSeperator + "src\\Reports";
    private static final String screenshotFilepath = System.getProperty("user.dir") + fileSeperator + "src\\Reports\\Screenshots";
    private static final String allureResultsPath= System.getProperty("user.dir") + fileSeperator +"allure-results";
    private static String reportFileLocation = reportFilepath + fileSeperator + reportFileName;
    public static String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    private static String testDetailFilepath = System.getProperty("user.dir") + fileSeperator + "src\\Reports\\TestDetails";
    private static String apiTestDetailsFilePath = testDetailFilepath + fileSeperator + "APITestDetails.txt";
    private static String webTestDetailsFilePath = testDetailFilepath + fileSeperator + "WebTestDetails.txt";
    private static FileWriter fileWriter;

    public static void cleanDirectory() throws IOException {
        File f2 = new File(screenshotFilepath);
        FileUtils.cleanDirectory(f2);

        File f3= new File(allureResultsPath);
        FileUtils.cleanDirectory(f3);

    }

    public static ExtentReports getInstance() {
        if (extentReport == null)
            createInstance();
        return extentReport;
    }

    public static ExtentReports createInstance() {
        System.out.println("file path: " + reportFileLocation);
        String fileName = getReportPath(reportFilepath);
        htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle(reportFileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportFileName);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        extentReport = new ExtentReports();
        extentReport.attachReporter(htmlReporter);
        return extentReport;
    }

    private static String getReportPath(String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!");
                return reportFileLocation;
            } else {
                System.out.println("Failed to create directory: " + path);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
        return reportFileLocation;
    }


    public static synchronized ExtentTest getTest() {
        //return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized void endTest() {
        extentReport.flush();
    }

    public static synchronized ExtentTest startTest(String testName) {
        extentReport = getInstance();
        extentTest = extentReport.createTest(testName);
        System.out.println("extentTest: "+extentTest);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), extentTest);
        System.out.println(extentTestMap);
        return extentTest;
    }

    public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
        File f = new File(screenshotFilepath);
        if (!f.exists()) {
            if (f.mkdir())
                getTest().info("Directory: " + f.getAbsolutePath() + " is created!");
            else {
                getTest().info("Failed to create directory: " + f.getAbsolutePath());
            }
        } else {
            System.out.println("Directory already exists at: " + f.getAbsolutePath());
        }
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = screenshotFilepath + fileSeperator + screenshotName + "_" + dateName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }

    public static String getCustomStackTrace(Throwable aThrowable) {
        final StringBuilder result = new StringBuilder( "Exception is: " );
        result.append(aThrowable.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);
        for (StackTraceElement element : aThrowable.getStackTrace() ){
            result.append( element );
            result.append( NEW_LINE );
        }
        return result.toString();
    }

    public static String getFullPageShutterbug(WebDriver driver,String ScreenshotName) throws IOException {
        File f1 = new File(screenshotFilepath);
        if (!f1.exists()) {
            if (f1.mkdir())
                getTest().info("Directory: " + f1.getAbsolutePath() + " is created!");
            else {
                getTest().info("Failed to create directory: " + f1.getAbsolutePath());
            }
        } else {
            System.out.println("Directory already exists at: " + f1.getAbsolutePath());
        }
        String destination = screenshotFilepath + fileSeperator ;
        Shutterbug.shootPage(driver, ScrollStrategy.WHOLE_PAGE).withName(ScreenshotName + "_" + dateName).save(destination);
        return destination+ScreenshotName + "_" + dateName + ".png";
    }

    public static void addTestCountDetailsToFile(HashMap<String,String> hmap,int tcPassed, int tcFailed) throws IOException {
        String testType= getTestType();
        String testDetailsPath= getTestDetailFilepath(reportFilepath,testType);
        fileWriter= new FileWriter(testDetailsPath);
        int tcTotal= tcPassed+tcFailed;
        for (Map.Entry<String, String> entry : hmap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            fileWriter.write("TC_Name:"+entry.getKey()+",");
            fileWriter.write("Bug_Id:"+entry.getValue()+System.getProperty("line.separator"));
        }
        fileWriter.write("TC_Total:"+tcTotal+",");
        fileWriter.write("TC_Passed:"+tcPassed+",");
        fileWriter.write("TC_Failed:"+tcFailed);
        fileWriter.close();
    }

    public static String getTestDetailFilepath(String filePath, String testType){
        File testDirectory = new File(filePath);
        String testDetailPath;
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + filePath + " is created!");
                return reportFileLocation;
            } else {
                System.out.println("Failed to create directory: " + filePath);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("Directory already exists: " + filePath);
        }
        switch(testType){
            case "API":
                System.out.println("API Test detail file path: "+apiTestDetailsFilePath);
                testDetailPath= apiTestDetailsFilePath;
                break;
            case "WEB":
                System.out.println("WEB Test detail file path: "+webTestDetailsFilePath);
                testDetailPath= webTestDetailsFilePath;
                break;
            default:
                testDetailPath= webTestDetailsFilePath;
        }
        return testDetailPath;
    }

    public static String getTestType(){
        String testType= AllureExtentTestNGListener.testName;
            if (testType.toUpperCase().contains("API")){
                return "API";
            }else if (testType.toUpperCase().contains("WEB")){
                return "WEB";
        }else{
                return "WEB";
            }
    }


}