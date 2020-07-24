package web_selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

/**
 * Wraps a WebElement and exposes part of its API in a way that is safe to
 * access from JavaScript code. This class is what gets outputted by the
 * GetElements action.
 */
public class ElementWrapper {

    private transient WebElement element;

    private String tag;

    public String text;

    public ElementWrapper(WebElement element) {
        this.element = element;
        this.text = element.getText();
        this.tag = element.getTagName();
    }

    public String getAttribute(String attribute) {
        return element.getAttribute(attribute);
    }

    public Point getLocation() {
        return element.getLocation();
    }

    public Dimension getSize() {
        return element.getSize();
    }

    public String getTagName() {
        return element.getTagName();
    }

    public String getText() {
        this.text = element.getText();
        return this.text;
    }
    
    public boolean isDisplayed() {
        return element.isDisplayed();
    }
    
    public boolean isEnabled() {
        return element.isEnabled();
    }
    public boolean isSelected() {
        return element.isSelected();
    }
    
}
