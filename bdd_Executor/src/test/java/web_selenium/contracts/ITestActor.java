package web_selenium.contracts;


import web_selenium.util.Config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Observer;

public interface ITestActor {

    public void addObserver(Observer o);

    public void close();

    public void deleteObserver(Observer o);

    /**
     *
     * Evaluates a script using the test actor's script engine and returns the
     * result of the evaluation.
     */
    public Object evalScript(String script);

    /**
     * Returns the test actor configuration.
     */
    public Config getConfig();
    
    /**
     * Queries the sync service for the specified image file.
     */
    public BufferedImage getImage(String fileName);

    /**
     * Returns the directory where the test actor saves any files it generates
     * at runtime (screenshots, artifacts for debugging, etc).
     */
    File getOutDir();

    /**
     * Returns the directory where the test actor saves the screenshots captured
     * during test execution .
     */
    File getScreenshotsDir();

    /**
     * Returns the temporary directory used by the test actor. This is where
     * test actions can store temporary files and any kind of persistent data.
     */
    File getTempDir();

    /**
     * Returns a test asset of the given type (test definition, macro, data
     * file, image, etc) by requesting it from the sync service.
     */
    InputStream getTestAsset(String assetType, String partialPath);

    public String getType();

    /**
     * Add a variable into the JS engine's global scope.
     */
    public void injectVariable(String varName, Object varValue);

    /**
     * Parses JSON data using the JS engine and returns the corresponding JS
     * native value.
     */
    public Object parseJsonToNativeJsType(String jsonData);

    public void runOneSession(Duration maxWaitTime);

    public void runOneSession();

    /**
     * Converts the provided argument to a native JavaScript array. Useful when
     * returning Java collection types from JS APIs, so the JS code can work
     * with the collection using the JS Array APIs. If the value is not
     * "array-like", the method will return a new array with the input value as
     * its only element.
     */
    public Object toJsArray(Object arrayLike);

    public Object toJsType(Object inputValue);
}
