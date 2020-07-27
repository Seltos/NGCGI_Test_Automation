package com.enquero.driverfactory.selenium_web_util;

import com.paulhammant.ngwebdriver.NgWebDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
public abstract class SeleniumTestAction extends TestAction{

    protected WebDriver driver;

    @Override
    public void run() {
        super.run();
    }

    protected void waitForAsyncCallsToFinish() {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) this.driver;
            NgWebDriver ngDriver = new NgWebDriver(jsExecutor);
            ngDriver.waitForAngularRequestsToFinish();
        } catch (Exception ex) {
            log.warn("The waitForAsyncCallsToFinish method failed.", ex);
        }
    }

    public InputStream takeScreenshot() {
        try {
            //TODO: Maybe try to use robot to capture the whole desktop when alert is present
            // In some browsers, screenshots don't work when alert is present,
            // so we attempt to dismiss a potential alert in order to avoid
            // weird behavior or blocking the test forever
            try {
                // This logic doesn't curently work for Chrome 61.0.3163.100 (64-bit)
                WebDriverWait wait = new WebDriverWait(driver, 1);
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.dismiss();
            } catch (Throwable ex) {
            }
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            return new ByteArrayInputStream(screenshotBytes);
        } catch (Exception ex) {
            return null;
        }
    }
}
