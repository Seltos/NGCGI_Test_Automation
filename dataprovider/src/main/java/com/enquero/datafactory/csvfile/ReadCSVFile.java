package com.enquero.datafactory.csvfile;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReadCSVFile {

    /**
     * @param csvFilepath the CSV file path which need to read
     * @param methodName the method name is the specified Test Casename
     * @return Iterator<Object[]>
     */
    public Iterator<Object[]> getTestData(String csvFilepath,String methodName){
        Collection<Object[]> provider = new ArrayList<Object[]>();
        TestDataFactory dataFactory= new TestDataFactory();
        String line="";
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFilepath));
            while ((line = br.readLine()) != null)
            {
                String[] testData = line.split(";");
                for (int j = 0; j < testData.length; j++) {
                    if (testData[j].equalsIgnoreCase(methodName)) {
                        dataFactory.setTestCaseId(testData[0]);
                        dataFactory.setTestCaseName(testData[j]);
                        String input= testData[j+1];
                        JSONObject json = returnJson(input);
                        JSONObject inputParameters = new JSONObject(json);
                        dataFactory.setInputParameters(inputParameters);
                        String validate= testData[j+2];
                        JSONObject json1 = returnJson(validate);
                        JSONObject validateParameters = new JSONObject(json1);
                        dataFactory.setValidationParameters(validateParameters);
                        provider.add(new Object[]{dataFactory});
                    }
                }
            }
    }catch(Exception e){
            e.printStackTrace();
        }
        return provider.iterator();
    }

    /**
     * @param text the String text which need to be converted to JSONObject
     * @return JSONObject
     */
    public JSONObject returnJson(String text){
       String[] inputs=  text.split(",");
        JSONObject json= new JSONObject();
       for (int i=0;i<inputs.length;i++){
           String[] inputArr1= inputs[i].split(":");
           for (int j=0;j<inputArr1.length;j++){
               json.put(inputArr1[j].replaceAll("\"",""),inputArr1[j+1].replaceAll("\"",""));
               break;
           }
       }
    return json;
    }

}
