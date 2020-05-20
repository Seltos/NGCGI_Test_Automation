package com.enquero.datafactory.jsonfile;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReadJsonFile {

    /**
     * @param jsonFilepath the path of the Json File.
     * @param methodName the testcase name
     * @return the Iterator<Object[]>
     */
    public Iterator<Object[]> getTestData(String jsonFilepath,String methodName){
        Collection<Object[]> provider = new ArrayList<Object[]>();
        JSONParser jsonParser = new JSONParser();
        try {
            TestDataFactory dataFactory = new TestDataFactory();
            FileReader reader = new FileReader(jsonFilepath);
            Object obj = jsonParser.parse(reader);
            JSONObject json=  (JSONObject) obj;
            JSONObject json1=  (JSONObject) json.get("tests");
            JSONArray jsonArr= (JSONArray) json1.get("test");
          for (int i=0;i<jsonArr.size();i++) {
              JSONObject json12 = (JSONObject) jsonArr.get(i);
              String method= (String) json12.get("testcaseName");
              if (method.equalsIgnoreCase(methodName)) {
                  dataFactory.setTestCaseId((String) json12.get("testcaseId"));
                  dataFactory.setTestCaseName((String) json12.get("testcaseName"));
                  JSONObject inputParameters = (JSONObject )json12.get("InputParameters");
                  JSONObject validationParameters = (JSONObject) json12.get("ValidationParameters");
                  dataFactory.setInputParameters(inputParameters);
                  dataFactory.setValidationParameters(validationParameters);
                  provider.add(new Object[]{dataFactory});
              }
          }
        }catch(Exception e){
            e.printStackTrace();
        }
        return provider.iterator();
    }
}
