package web_selenium;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import web_selenium.logging.Logger;
import web_selenium.util.Config;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumHelper {

    private static Config config;

    private static WebDriver driver;

    private static int explicitWaitSec;

    static {
        config = Config.load("actor.yaml");
        explicitWaitSec = config.getInteger("selenium.explicitWaitSec", 10);
    }

    private static WebDriver createDriver() {
        WebDriver webDriver = null;
        DesiredCapabilities caps;

        setSystemProperties();

        if (config.hasProperty("selenium.seleniumServerUrl")) {
            String seleniumServerUrl = config.getString("selenium.seleniumServerUrl");
            Logger.info(String.format("Using remote Selenium server %s", seleniumServerUrl));
            try {
                caps = new DesiredCapabilities();
                injectCapsFromConfig(caps);
                webDriver = new RemoteWebDriver(new URL(seleniumServerUrl), caps);
            } catch (Exception ex) {
                throw new RuntimeException(String.format(
                        "Falied to connect to Selenium server %s",
                        seleniumServerUrl), ex);
            }
        } else {
            Logger.info("Using local Selenium server");
            String browserName = config.getString("selenium.desiredCapabilities.browserName").toLowerCase();
            switch (browserName) {
                case "chrome":
                    setDriverExecutable(config.getString("selenium.chromeDriverExePath", null), "chrome");
                    caps = getCapsForBrowser("chrome");
                    injectCapsFromConfig(caps);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.merge(caps);

                    if (config.hasProperty("selenium.chromeDriverExeArgs")) {
                        chromeOptions.addArguments(config.getList("selenium.chromeDriverExeArgs"));
                    }

                    if (config.hasProperty("selenium.chromeAcceptInsecureCerts")) {
                        chromeOptions.setAcceptInsecureCerts(config.getBoolean("selenium.chromeAcceptInsecureCerts"));
                    }

                    if (config.hasProperty("selenium.chromeExperimentalOptions")) {
                        config.getMap("selenium.chromeExperimentalOptions").forEach((key, value) -> {
                            chromeOptions.setExperimentalOption(key.toString(), value);
                        });
                    }

                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    setDriverExecutable(config.getString("selenium.edgeDriverExePath", null), "edge");
                    caps = getCapsForBrowser("edge");
                    injectCapsFromConfig(caps);
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions
                            .merge(caps);
                    webDriver = new EdgeDriver(edgeOptions);
                    break;
                case "firefox":
                    setDriverExecutable(config.getString("selenium.firefoxDriverExePath", null), "firefox");
                    caps = getCapsForBrowser("firefox");
                    injectCapsFromConfig(caps);
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.merge(caps);
                    if (config.hasProperty("selenium.firefoxDriverExeArgs")) {
                        firefoxOptions.addArguments(config.getList("selenium.firefoxDriverExeArgs"));
                    }
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;
                /*case "ie":
                case "internet explorer":
                    setDriverExecutable(config.getString("selenium.ieDriverExePath", null), "internet explorer");
                    caps = getCapsForBrowser("internet explorer");
                    // Avoid the browser zoom level error
                    caps.setCapability("ignoreZoomSetting", true);
                    injectCapsFromConfig(caps);
                    InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                    ieOptions.merge(caps);
                    if (config.hasProperty("selenium.ieDriverExeArgs")) {
                        ieOptions.addCommandSwitches((String[]) config.getList("selenium.ieDriverExeArgs").toArray());
                    }
                    webDriver = new InternetExplorerDriver(ieOptions);
                    break;*/
                case "opera":
                    setDriverExecutable(config.getString("selenium.operaDriverExePath", null), "opera");
                    caps = getCapsForBrowser("opera");
                    injectCapsFromConfig(caps);
                    OperaOptions operaOptions = new OperaOptions();
                    operaOptions.merge(caps);
                    if (config.hasProperty("selenium.operaDriverExeArgs")) {
                        operaOptions.addArguments(config.getList("selenium.operaDriverExeArgs"));
                    }
                    webDriver = new OperaDriver(operaOptions);
                    break;
                case "safari":
                    caps = getCapsForBrowser("safari");
                    injectCapsFromConfig(caps);
                    SafariOptions safariOptions = new SafariOptions(caps);
                    webDriver = new SafariDriver(safariOptions);
                    safariOptions.setUseTechnologyPreview(config.getBoolean("selenium.safariUseTechnologyPreview", false));
                    break;
                default:
                    throw new RuntimeException(String.format(
                            "The \"selenium.browserName\" config property specifies a browser "
                            + "that is invalid or not supported. The property value was \"%s\". "
                            + "The valid values are: \"chrome\", \"edge\", \"firefox\", \"internet "
                            + "explorer\", \"opera\" and \"safari\".",
                            browserName));
            }
        }

        webDriver.manage().timeouts().setScriptTimeout(config.getInteger("selenium.scriptTimeout", 20), TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        Boolean maximizeWindow = config.getBoolean("selenium.maximizeWindow", !SystemUtils.IS_OS_MAC);
        Boolean fullScreen = config.getBoolean("selenium.fullScreen", null);
        Rectangle resolution = SeleniumHelper.parseResolution(config.getString("selenium.resolution", ""));
        if (resolution != null) {
            webDriver.manage().window().setPosition(new Point(resolution.x, resolution.y));
            webDriver.manage().window().setSize(new Dimension(resolution.width, resolution.height));
        } else {
            if (maximizeWindow && !Boolean.TRUE.equals(fullScreen)) {
                try {
                    webDriver.manage().window().maximize();
                } catch (Exception ex) {
                    Logger.warning(String.format("Failed to maximize browser window"), ex);
                }
            }

            if (fullScreen != null && fullScreen) {
                try {
                    if (maximizeWindow && SystemUtils.IS_OS_MAC) {
                        // We have to wait for the macOS window resize animation to
                        // finish, otherwise the full-screen won't take
                        Thread.sleep(1000);
                    }
                    webDriver.manage().window().fullscreen();
                } catch (Exception ex) {
                    Logger.warning(String.format("Failed to set full-sceen browser mode"), ex);
                }
            }
        }

        return webDriver;
    }

    private static DesiredCapabilities getCapsForBrowser(String browserName) {
        DesiredCapabilities caps;

        switch (browserName) {
            case "chrome":
                caps = DesiredCapabilities.chrome();
                break;
            case "edge":
                caps = DesiredCapabilities.edge();
                break;
            case "firefox":
                caps = DesiredCapabilities.firefox();
                break;
            case "internet explorer":
                caps = DesiredCapabilities.internetExplorer();
                // Avoid the browser zoom level error
                caps.setCapability("ignoreZoomSetting", true);
                break;
            case "safari":
                caps = DesiredCapabilities.safari();
                break;
            default:
                caps = new DesiredCapabilities();
        }

        return caps;
    }

    public static Config getConfig() {
        return config;
    }

    /**
     * Returns the default Selenium explicit wait time in seconds.
     *
     * @return Explicit wait time in seconds
     */
    public static int getExplicitWaitSec() {
        return explicitWaitSec;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
        }

        return driver;
    }

    /**
     * Inject desired capabilities from configuration into the specified
     * DesiredCapabilities object.
     */
    private static void injectCapsFromConfig(DesiredCapabilities caps) {
        Object capsFromConfig = config.get("selenium.desiredCapabilities");

        if (capsFromConfig instanceof Map) {
            Map<String, Object> capsFromConfigMap = (Map) capsFromConfig;
            for (Map.Entry entry : capsFromConfigMap.entrySet()) {
                if (entry.getValue() != null) {
                    caps.setCapability(entry.getKey().toString(), entry.getValue());
                }
            }
        }
    }

    /**
     * Calls driver.quit() and sets the driver instance to null.
     */
    public static void discardDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ex) {
                Logger.warning("Failed to quit the Selenium driver", ex);
            }

            driver = null;
        }
    }

    public static Rectangle parseResolution(String resolutionStr) {
        Rectangle resolution;
        // RegEx example 1: 1024x768
        // RegEx example 2: 0,0,1024,768
        Pattern pattern = Pattern.compile(
                "\\s*((?<x>\\d+)[\\sx,]+(?<y>\\d+)[\\sx,]+)?(?<width>\\d+)[\\sx,]+(?<height>\\d+)\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(resolutionStr);
        if (matcher.find()) {
            Integer x = matcher.group("x") != null ? Integer.parseInt(matcher.group("x")) : 0;
            Integer y = matcher.group("y") != null ? Integer.parseInt(matcher.group("y")) : 0;
            Integer width = matcher.group("width") != null ? Integer.parseInt(matcher.group("width")) : 0;
            Integer height = matcher.group("height") != null ? Integer.parseInt(matcher.group("height")) : 0;
            return new Rectangle(x, y, width, height);
        } else {
            return null;
        }
    }

    /**
     * Sets the "webdriver.BROWSER_NAME.driver" system property for the
     * specified browser.
     */
    private static void setDriverExecutable(String executablePath, String browserName) {
        if (executablePath == null || executablePath.trim().isEmpty()) {
            return;
        }

        executablePath = executablePath.trim();
        File executableFile = new File(executablePath);
        if (!executableFile.exists()) {
            throw new RuntimeException(String.format(
                    "Failed to find the Selenium driver executable for browser \"%s\". "
                    + "File \"%s\" does not exist. You can provide the correct path to "
                    + "the driver in the test actor's configuration file.",
                    browserName,
                    executablePath));
        }

        switch (browserName) {
            case "chrome":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.chrome.driver",
                        executablePath));
                System.setProperty("webdriver.chrome.driver", executablePath);
                break;
            case "edge":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.edge.driver",
                        executablePath));
                System.setProperty("webdriver.edge.driver", executablePath);
                break;
            case "firefox":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.firefox.driver",
                        executablePath));
                System.setProperty("webdriver.gecko.driver", executablePath);
                break;
            case "internet explorer":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.ie.driver",
                        executablePath));
                System.setProperty("webdriver.ie.driver", executablePath);
                break;
            case "opera":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.opera.driver",
                        executablePath));
                System.setProperty("webdriver.opera.driver", executablePath);
                break;
            case "safari":
                Logger.trace(String.format("Setting system property \"%s\" to \"%s\"",
                        "webdriver.safari.driver",
                        executablePath));
                System.setProperty("webdriver.safari.driver", executablePath);
                break;
            default:
                throw new RuntimeException(String.format(
                        "Browser \"%s\" is not currently supported",
                        browserName));
        }
    }

    /**
     * Set the system properties as specified in configuration property
     * "selenium.systemProperties".
     */
    private static void setSystemProperties() {
        Object systemProperties = config.get("selenium.systemProperties", null);

        if (systemProperties instanceof Map) {
            Map<String, Object> propsAsMap = (Map) systemProperties;
            for (Map.Entry entry : propsAsMap.entrySet()) {
                if (entry.getValue() != null) {
                    System.setProperty(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        }
    }

    /**
     * Check the connection with the driver and perform a simple operation to
     * ensure it works properly. Discard the driver instance and create a new
     * one, if necessary.
     */
    public static void verifyDriverIsValid() {
        int retriesLeft = 3;

        while (retriesLeft > 0) {
            retriesLeft--;

            try {
                if (driver == null) {
                    driver = createDriver();
                }
                // Just an operation that requires communication with the driver
                driver.manage().getCookieNamed("no-cookie-for-you");
                break;
            } catch (Throwable ex) {
                discardDriver();

                Logger.info(String.format("Caught exception %s while initializing the Selenium driver. The exception message was: \"%s\". Retries left: %s.",
                        ex.getClass().getName(),
                        ex.getMessage(),
                        retriesLeft));
            }
        }
    }
}
