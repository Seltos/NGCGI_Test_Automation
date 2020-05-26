package ExtentReports_Listeners.ExtReporter;

import Base.BaseUtil;
import Base.base;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExtentTestReporter extends BaseUtil {


    String fileName = System.getProperty("user.dir")+"\\Report\\" + "Extent_Report.html";
    public static String reportLocation = System.getProperty("user.dir")+"\\Screenshots\\";

    public ExtentTest ExtentReport() {
        System.out.println("Report path"+fileName);
        //First is to create Extent Reports
        extentReport = new ExtentReports();
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Test report for Selenium Basic");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Test report");
        extentReport.attachReporter(htmlReporter);
        features= extentReport.createTest(Feature.class, "Login Feature");
        return features;
    }

    public void ExtentReportScreenshot() throws IOException {
        File file1= new File(reportLocation);
        FileUtils.cleanDirectory(file1);
        File scr = ((TakesScreenshot) base.initializeDriver()).getScreenshotAs(OutputType.FILE);
        Files.copy(scr.toPath(), new File(reportLocation + "screenshot.png").toPath());
        scenarioDef.fail("details").addScreenCaptureFromPath(reportLocation + "screenshot.png");
    }


    public void FlushReport(){
        extentReport.flush();
    }


/*

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

   // @Step("to get the Report path")
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


    public static ExtentTest getTest() {
        //return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static void endTest() {
        extentReport.flush();
    }

    public static ExtentTest startTest(String testName) {
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
        FileUtils.cleanDirectory(f);
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
*/

}
