package com.enquero.driverfactory.web_selenium;

import com.enquero.driverfactory.web_selenium.base.TestAction;
import com.enquero.driverfactory.web_selenium.base.TestActorEvents;
import com.enquero.driverfactory.web_selenium.contracts.ITestActor;
import com.enquero.driverfactory.web_selenium.logging.Logger;
import com.enquero.driverfactory.web_selenium.util.Config;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public abstract class SeleniumTestAction extends TestAction {


    // Used for implementing the Selenium API for emulating
    // complex user gestures
    private static Actions actions;

    protected static Config config;

    // Stores a reference to the Selenium WebDriver object
    // used for automation.
    protected WebDriver driver;

    /**
     * Just a static flag to remember whether we've done the one-time
     * initialization work in the initialize() method.
     */
    private static boolean initialized;

    public static void discardActionsObject() {
        SeleniumTestAction.actions = null;
    }

    /**
     * Locates and returns a UI element.
     */
    protected WebElement getElement(By locator) {
        return getElement(locator, this.getExplicitWaitSec());
    }

    /**
     * Locates and returns a UI element.
     */
    protected WebElement getElement(By locator, int explicitWaitSec) {
        WebDriverWait wait = new WebDriverWait(driver, explicitWaitSec);
        WebElement element = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator));
        return element;
    }

    /**
     * Locates and returns a set of UI elements specified by a locator that
     * matches multiple elements.
     */
    protected List<WebElement> getElements(By locator) {
        return getElements(locator, this.getExplicitWaitSec());
    }

    /**
     * Locates and returns a set of UI elements specified by a locator that
     * matches multiple elements.
     */
    protected List<WebElement> getElements(By locator, int timeoutSec) {
        // Never wait for more than 2 minutes
        timeoutSec = Math.min(timeoutSec, 120);

        long startTime = System.currentTimeMillis();

        while (true) {
            try {
                return driver.findElements(locator);
            } catch (Exception ex1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex2) {
                }

                if ((System.currentTimeMillis() - startTime) > timeoutSec * 1000) {
                    throw ex1;
                }
            }
        }
    }

    /**
     * Returns the number of seconds provided in the explicitWaitSec argument
     * for the current test action, or the default value specified in the
     * selenium.explicitWaitSec config parameter.
     */
    public int getExplicitWaitSec() {
        return this.readIntArgument("explicitWaitSec", SeleniumHelper.getExplicitWaitSec());
    }

    /**
     * Creates a new Actions object (if it doesn't exist yet) and returns it.
     * The Actions class is part of the Selenium user-facing API for emulating
     * complex user gestures.
     */
    public static Actions getActionsInstance() {
        if (SeleniumTestAction.actions == null) {
            SeleniumTestAction.actions = new Actions(SeleniumHelper.getDriver());
        }

        return SeleniumTestAction.actions;
    }

    /**
     * Converts a locator value from a Map containing the locator information to
     * an org.openqa.selenium.By.
     */
    protected By getLocator(Object objLocator) {
        if (!(objLocator instanceof Map)) {
            throw new RuntimeException(String.format(
                    "Invalid locator: %s. Locators are expected to be passed as "
                            + "objects that contain at least one of the following "
                            + "properties: id, name, text, class, tag, css or xpath.",
                    objLocator));
        }

        Map<String, Object> argValueAsMap = (Map<String, Object>) objLocator;

        if (argValueAsMap.containsKey("id")) {
            return By.id(argValueAsMap.get("id").toString());
        } else if (argValueAsMap.containsKey("name")) {
            return By.name(argValueAsMap.get("name").toString());
        } else if (argValueAsMap.containsKey("class")) {
            return By.className(argValueAsMap.get("class").toString());
        } else if (argValueAsMap.containsKey("tag")) {
            return By.tagName(argValueAsMap.get("tag").toString());
        } else if (argValueAsMap.containsKey("css")) {
            return By.cssSelector(argValueAsMap.get("css").toString());
        } else if (argValueAsMap.containsKey("linkText")) {
            return By.linkText(argValueAsMap.get("linkText").toString());
        } else if (argValueAsMap.containsKey("partialLinkText")) {
            return By.partialLinkText(argValueAsMap.get("partialLinkText").toString());
        } else if (argValueAsMap.containsKey("xpath")) {
            if (argValueAsMap.get("xpath").toString().contains("''")) {
                return By.xpath(argValueAsMap.get("xpath").toString().replace("''", "'"));
            } else {
                return By.xpath(argValueAsMap.get("xpath").toString());
            }
        } else {
            throw new RuntimeException(
                    "You must identify the web element by providing at least one of the "
                            + "following properties: id, name, class, tag, css, linkText, partialLinkText or xpath.");
        }
    }

    @Override
    public void initialize() {
        super.initialize();

        if (!SeleniumTestAction.initialized) {
            SeleniumTestAction.initialized = true;
            SeleniumTestAction.config = SeleniumHelper.getConfig();

            this.getActor().addObserver(new Observer() {
                @Override
                public void update(Observable eventSource, Object eventData) {
                    if (eventSource instanceof ITestActor) {
                        if (eventData == TestActorEvents.TEST_COMPLETED) {
                            try {
                                List<LogEntry> logEntries = SeleniumHelper.getDriver().manage().logs().get("browser").getAll();
                                if (logEntries.size() > 0) {
                                    StringBuffer strBuffer = new StringBuffer();
                                    for (LogEntry logEntry : logEntries) {
                                        strBuffer.append(String.format(" >>>>> LEVEL: %s | TIMESTAMP: %s | MESSAGE: %s",
                                                logEntry.getLevel().getName(),
                                                logEntry.getTimestamp(),
                                                logEntry.getMessage()));

                                    }
                                    log.debug("The browser log entries were: \n" + strBuffer.toString());
                                } else {
                                    log.info("There were no entries in the browser log. ");
                                }
                            } catch (Exception exc) {
                                log.warning("Failed to get browser log entries. ", exc);
                            }

                            SeleniumTestAction.discardActionsObject();
                            if (!SeleniumHelper.getConfig().getBoolean("selenium.reuseDriver", false)) {
                                SeleniumHelper.discardDriver();
                            }
                        }
                    }
                }
            });
        }

        this.driver = SeleniumHelper.getDriver();
    }

    @Override
    public void run() {
        super.run();
    }

    /**
     * Reads the argument value for the specified key, as a By instance.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public By readLocatorArgument(String argName, By defaultValue) {
        if (this.hasArgument(argName)) {
            return this.readLocatorArgument(argName);
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a By instance. The
     * element is identified by xpath, id, name or text.
     *
     * @param argName The argument whose value will be returned
     */
    protected By readLocatorArgument(String argName) {
        Object argumentValue = readArgument(argName);
        return getLocator(argumentValue);
    }

    /**
     * Reads the argument value for the specified key, as type By[].
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public By[] readLocatorArguments(String argName) {
        return this.readLocatorArguments(argName, new By[0]);
    }

    /**
     * Reads the argument value for the specified key, as type By[].
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public By[] readLocatorArguments(String argName, By[] defaultValue) {
        if (this.hasArgument(argName)) {
            Object argValue = this.readArgument(argName);
            if (argValue instanceof List) {
                List<Object> objList = (List<Object>) argValue;

                By[] locators = new By[objList.size()];
                for (int i = 0; i < objList.size(); i++) {
                    locators[i] = getLocator(objList.get(i));
                }
                return locators;
            } else {
                By[] locators = new By[1];
                locators[0] = this.readLocatorArgument(argName);
                return locators;
            }
        } else {
            return defaultValue;
        }
    }

    protected void scroll(String scrollDirection) {
        Logger.trace(String.format("WebTestAction.scroll (%s)", scrollDirection));
        JavascriptExecutor jse;
        jse = (JavascriptExecutor) driver;

        if (scrollDirection.equalsIgnoreCase("down")) {

            jse.executeScript("window.scrollBy(0,250)", "");

        } else if (scrollDirection.equalsIgnoreCase("up")) {
            jse.executeScript("window.scrollBy(0,-250)", "");
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

    /**
     * Waits for JavaScript asynchronous logic in the web page to finish
     * (Angular, React, etc).
     */
    protected void waitForAsyncCallsToFinish() {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) this.driver;
            NgWebDriver ngDriver = new NgWebDriver(jsExecutor);
            ngDriver.waitForAngularRequestsToFinish();
        } catch (Exception ex) {
            log.warning("The waitForAsyncCallsToFinish method failed.", ex);
        }
    }

}
