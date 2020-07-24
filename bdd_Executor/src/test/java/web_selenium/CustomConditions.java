package web_selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.regex.Pattern;

public class CustomConditions {

    /**
     * Returns an ExpectedCondition instance that waits for an element to be
     * absent from the current page.
     */
    public static ExpectedCondition<Boolean> absenceOfElementLocated(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    driver.findElement(locator);
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element to not be present: " + locator;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits for the current page URL
     * to match the provided regular expression.
     */
    public static ExpectedCondition<Boolean> urlToMatch(final Pattern regexPattern) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String url = driver.getCurrentUrl();
                Boolean matches = regexPattern.matcher(url).matches();
                return matches;
            }

            @Override
            public String toString() {
                return "current page URL to match: " + regexPattern.toString();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an attribute's
     * text content is an exact match of the specified text.
     */
    public static ExpectedCondition<Boolean> elementAttributeToBe(final WebElement element, String attribute, String text, boolean caseInsensitive) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String value = element.getAttribute(attribute);

                if (caseInsensitive) {
                    return value.equalsIgnoreCase(text);
                } else {
                    return value.equals(text);
                }
            }

            @Override
            public String toString() {
                return "element text to be: " + text;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an attribute
     * exists on the specified element.
     */
    public static ExpectedCondition<Boolean> elementAttributeToBePresent(final WebElement element, String attribute) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String value = element.getAttribute(attribute);
                return value != null;
            }

            @Override
            public String toString() {
                return String.format("element to have an attribute named %s", attribute);
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an attribute's
     * value contains the specified text.
     */
    public static ExpectedCondition<Boolean> elementAttributeToContain(final WebElement element, String attribute, String text, boolean caseInsensitive) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String value = element.getAttribute(attribute);

                if (caseInsensitive) {
                    return value.toLowerCase().contains(text.toLowerCase());
                } else {
                    return value.contains(text);
                }
            }

            @Override
            public String toString() {
                return "element text to contain: " + text;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an attribute's
     * value matches the specified regular expression.
     */
    public static ExpectedCondition<Boolean> elementAttributeToMatch(final WebElement element, String attribute, Pattern regex) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return regex.matcher(element.getAttribute(attribute)).find();
            }

            @Override
            public String toString() {
                return "element text to match: " + regex.pattern();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that tries to click an element and
     * returns false if the click failed.
     */
    public static ExpectedCondition<Boolean> elementWasClicked(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    element.click();
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to be clicked: " + element.toString();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element becomes
     * the active (in-focus) element.
     */
    public static ExpectedCondition<Boolean> elementToBeActive(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (element.equals((driver.switchTo().activeElement()))) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to be active (in focus): " + element.toString();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element becomes
     * the active (in-focus) element.
     */
    public static ExpectedCondition<Boolean> elementToHaveClass(final WebElement element, String className) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String classAttribute = element.getAttribute("class");

                    boolean foundClass = false;

                    for (String currentClass : classAttribute.split(" ")) {
                        if (currentClass.equals(className)) {
                            return true;
                        }
                    }
                    
                    return false;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to have class: " + className;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element's text
     * content is an exact match of the specified text.
     */
    public static ExpectedCondition<Boolean> elementTextToBe(final WebElement element, String text, boolean caseInsensitive) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String elementText = element.getText();

                if (caseInsensitive) {
                    return elementText.equalsIgnoreCase(text);
                } else {
                    return elementText.equals(text);
                }
            }

            @Override
            public String toString() {
                return "element text to be: " + text;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element's text
     * content contains the specified text.
     */
    public static ExpectedCondition<Boolean> elementTextToContain(final WebElement element, String text, boolean caseInsensitive) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String elementText = element.getText();

                if (caseInsensitive) {
                    return elementText.toLowerCase().contains(text.toLowerCase());
                } else {
                    return elementText.contains(text);
                }
            }

            @Override
            public String toString() {
                return "element text to contain: " + text;
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element's text
     * content matches the specified regular expression.
     */
    public static ExpectedCondition<Boolean> elementTextToMatch(final WebElement element, Pattern regex) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return regex.matcher(element.getText()).find();
            }

            @Override
            public String toString() {
                return "element text to match: " + regex.pattern();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element becomes
     * disabled (generally applies to input elements).
     */
    public static ExpectedCondition<Boolean> elementToBeDisabled(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (!element.isEnabled()) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to be disabled: " + element.toString();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element becomes
     * enabled (generally applies to input elements).
     */
    public static ExpectedCondition<Boolean> elementToBeEnabled(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (element.isEnabled()) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to be enabled: " + element.toString();
            }
        };
    }

    /**
     * Returns an ExpectedCondition instance that waits until an element is not
     * active (in focus).
     */
    public static ExpectedCondition<Boolean> elementToNotBeActive(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (!element.equals((driver.switchTo().activeElement()))) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to not be active (in focus): " + element.toString();
            }
        };
    }
}
