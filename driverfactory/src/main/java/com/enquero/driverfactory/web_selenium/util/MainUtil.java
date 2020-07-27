package com.enquero.driverfactory.web_selenium.util;
import com.enquero.driverfactory.web_selenium.logging.Logger;

import java.io.File;
import java.security.CodeSource;

public class MainUtil {
    public static Class<?> getMainClass() throws ClassNotFoundException {
        StackTraceElement[] elements = new Exception().getStackTrace();
        return Class.forName(elements[elements.length - 1].getClassName());
    }
    
    public static File getMainJar() {
        try {
            Class<?> mainClass = getMainClass();

            CodeSource codeSource = mainClass.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            Logger.trace(String.format("The main JAR file is %s", jarFile.getCanonicalPath()));
            
            return jarFile;
        } catch (Exception ex) {
            throw new RuntimeException("Could not locate main JAR file", ex);
        }
    }
}
