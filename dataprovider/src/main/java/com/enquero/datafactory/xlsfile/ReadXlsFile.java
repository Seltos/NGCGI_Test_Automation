package com.enquero.datafactory.xlsfile;

import com.enquero.datafactory.DataFactory.TestDataFactory;
import org.apache.poi.ss.usermodel.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReadXlsFile {

    public Iterator<Object[]> getTestData(String xlsFilepath, String sheetName,String methodName){
        Collection<Object[]> provider = new ArrayList<Object[]>();
        try {
            TestDataFactory dataFactory = new TestDataFactory();
            File xlsFile = new File(xlsFilepath);
            FileInputStream fileInputStream = new FileInputStream(xlsFile);
            Workbook wb = WorkbookFactory.create(fileInputStream);
            Sheet sheet = wb.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            String name=null;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String testCaseName = row.getCell(1).getStringCellValue();
                if (methodName.equalsIgnoreCase(testCaseName)) {
                    dataFactory.setTestCaseId(row.getCell(0).getStringCellValue());
                    dataFactory.setTestCaseName(row.getCell(1).getStringCellValue());
                    String inputString= row.getCell(2).getStringCellValue();
                    JSONParser parser = new JSONParser();
                    JSONObject input_Obj = (JSONObject) parser.parse(inputString);
                    dataFactory.setInputParameters(input_Obj);
                    String validateString= row.getCell(3).getStringCellValue();
                    JSONObject validation_Obj = (JSONObject) parser.parse(validateString);
                    dataFactory.setValidationParameters(validation_Obj);
                    provider.add(new Object[]{dataFactory});
                } else {
                   // System.out.println("Test case " + methodName + "is not enabled for Execution");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return provider.iterator();
    }
}
