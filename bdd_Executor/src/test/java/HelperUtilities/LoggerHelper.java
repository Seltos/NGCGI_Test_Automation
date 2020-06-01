package HelperUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerHelper {

    private static boolean root=false;

    public static Logger getLogger(Class cls){
        System.setProperty("my.log", System.getProperty("user.dir")+"//reports//BDDLogs//BDDLog.log");
        if(root){
            return Logger.getLogger(cls);
        }
        PropertyConfigurator.configure(System.getProperty("user.dir")+"//reports//BDDLogs//log4j.properties");

        root = true;
        return Logger.getLogger(cls);
    }


}
