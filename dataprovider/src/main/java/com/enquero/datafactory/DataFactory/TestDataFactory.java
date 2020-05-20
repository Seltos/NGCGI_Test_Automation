package com.enquero.datafactory.DataFactory;


import org.json.simple.JSONObject;

public class TestDataFactory {
    public String testCaseId;
    public String testCaseName;
    public JSONObject inputParameters;
    public JSONObject validationParameters;

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public JSONObject getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(JSONObject inputParameters) {
        this.inputParameters = inputParameters;
    }

    public JSONObject getValidationParameters() {
        return validationParameters;
    }

    public void setValidationParameters(JSONObject validationParameters) {
        this.validationParameters = validationParameters;
    }



}
