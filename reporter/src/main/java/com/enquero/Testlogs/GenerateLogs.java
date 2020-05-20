package com.enquero.Testlogs;

import org.apache.log4j.PropertyConfigurator;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GenerateLogs {
    private static Path root= Paths.get(System.getProperty("user.dir")).getParent();
    private static String propertyFileName = "log4j" + ".properties";
    private static final String fileSeperator = System.getProperty("file.separator");
    private static String propertyFilepath = root+fileSeperator+"reporter\\src\\main\\java\\com\\enquero\\Testlogs";
    private static String PropertyFileLocation;

    public static void loadLogPropertyFile() {
        File file;
        String filepath= getLogPath(propertyFilepath);
        try {
            System.out.println("logfilepath: "+filepath);
            file = new File(filepath);
            Properties props = new Properties();
            props.load(new FileInputStream(filepath));
            props.setProperty("log4j.appender.file.File",System.getProperty("user.dir")+fileSeperator+"src\\Logs\\Test.log");
            PropertyConfigurator.configure(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getLogPath(String path) {
        PropertyFileLocation=root+fileSeperator+"reporter\\src\\main\\java\\com\\enquero\\Testlogs"+fileSeperator+propertyFileName;
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!");
                return PropertyFileLocation;
            } else {
                System.out.println("Failed to create directory: " + path);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
        return PropertyFileLocation;
    }

}
