
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class Executor {

    public void executeTests(File testsFile, File paramFile) throws Exception {

        List<String> testsToRun = new ArrayList<>();

        String runParallelBy = "";
        Integer threadCount =0;
        String testGroups ="";

        try(FileInputStream fis = new FileInputStream(testsFile);Scanner sc= new Scanner(fis)){
            while(sc.hasNextLine()){
                testsToRun.add(sc.nextLine());
            }
        }

        try(FileInputStream fis = new FileInputStream(paramFile);Scanner sc= new Scanner(fis)){
            while(sc.hasNextLine()){
                runParallelBy = sc.nextLine();
                threadCount = sc.nextInt();
                testGroups = sc.nextLine();
            }
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }



        executeTests(testsToRun, runParallelBy, threadCount, testGroups);

    }


    public void executeTests(List<String> testsToRun, String runParallelBy, Integer threadCount, String testGroups){

        XmlSuite suite = new XmlSuite();
        suite.setName("Automation Test Suite");

        if(runParallelBy!=null && runParallelBy.equalsIgnoreCase("method")) {
            suite.setParallel(XmlSuite.ParallelMode.METHODS);
        }else if(runParallelBy!=null && runParallelBy.equalsIgnoreCase("class")) {
            suite.setParallel(XmlSuite.ParallelMode.CLASSES);
        }else if(runParallelBy!=null && runParallelBy.equalsIgnoreCase("test")) {
            suite.setParallel(XmlSuite.ParallelMode.TESTS);
        }

        suite.setThreadCount(threadCount);

        XmlTest test = new XmlTest(suite);
        test.setName("Custom tests");

        for(int i=0; i<testsToRun.size();i++){
            String[] strings = testsToRun.get(i).split(",");

            if(test.getClasses().size()>0&&test.getClasses().contains(new XmlClass(strings[1]))) {
                int j = test.getClasses().indexOf(new XmlClass(strings[1]));
                test.getClasses().get(j).getIncludedMethods().add(new XmlInclude(strings[0]));
            }else {
                XmlClass xmlClass = new XmlClass(strings[1]);

                List<XmlInclude> xmlIncludes = new ArrayList<>();
                xmlIncludes.add(new XmlInclude(strings[0]));
                xmlClass.setIncludedMethods(xmlIncludes);

                test.getClasses().add(xmlClass);
            }

        }

        TestNG myTestNg = new TestNG();
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);

        myTestNg.setXmlSuites(suites);
        myTestNg.run();
    }

}