package com.enquero.api.driscolls.tests;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.enquero.Testlogs.GenerateLogs;
import com.enquero.api.driscolls.APICommon.Endpoints;
import com.enquero.api.driscolls.pojo.createIssueDetails;
import com.enquero.api.driscolls.pojo.employeeDetails;
import com.enquero.apiutility.RestUtil;
import com.enquero.reporter.ExtentTestReporter;
import com.enquero.webapp.driscolls.tests.TestLoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

import static com.enquero.apiutility.RestUtil.deleteJsonPath;
import static com.enquero.apiutility.RestUtil.getStringResponse;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;


public class EmployeeTest {
    Logger logger= Logger.getLogger(TestLoginPage.class.getName());
    public ExtentReports extentReports;
    private Response res = null; //Response object
    private JsonPath jp = null; //JsonPath object
    public Properties pr ;
    public String baseURI ="";
    public String basePath ="";
    public String endpoint = "";
    public String queryParam = "";
    public static String empid ="";
//    public ExcelUtils excelutils;


    @BeforeTest
    public void beforeTest() throws IOException {
        System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
        pr = new Properties();
//        FileInputStream f = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/com/enquero/api/driscolls/Input.properties");
//        pr.load(f);
//        baseURI = pr.getProperty("baseURI");
//        basePath = pr.getProperty("basePath");
        baseURI = Endpoints.APP_BASE_URI;
        basePath = Endpoints.APP_BASE_PATH;
        RestAssured.baseURI = baseURI;
        GenerateLogs.loadLogPropertyFile();
        logger.info(Thread.currentThread().getName()+ "****************** Execution  Started ****************");
        // ExtentTestReporter.getTest();
    }

   @Test(priority = 0)
   @Severity(SeverityLevel.MINOR)
   @Description("Test Case description: Returns Employee details")
   @Story("Story name:to check if specified endpoint fetches employee details")
    public void getEmployeeDetails(){
       logger.info(Thread.currentThread().getName() +" Get Employee tc started");
        endpoint = Endpoints.EMPLOYEES;
        ExtentTestReporter.getTest().log(Status.INFO,"Base URI is : " + baseURI);
        JsonPath getEmpDetails = RestUtil.getRequest(baseURI, basePath, endpoint, queryParam);
        ExtentTestReporter.getTest().log(Status.INFO,"Get request is executed Successfully" );
       logger.info(Thread.currentThread().getName() +" Get request is executed Successfully");
        String status = getEmpDetails
                .getString("status");
        ExtentTestReporter.getTest().log(Status.INFO,"Response Body Status is : " + status);
        Assert.assertEquals("fail",status);
        ExtentTestReporter.getTest().log(Status.INFO,"Expected and Actual is matched");
        ExtentTestReporter.getTest().log(Status.INFO,"Test Passed Successfully");
       logger.info(Thread.currentThread().getName() +" Test Passed Successfully");
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.MINOR)
    @Description("Test Case description: Insert Employee details")
    @Story("Story name:to check if specified endpoint insert new records into employee details")
    public void postEmployeeDetails(){
        logger.info(Thread.currentThread().getName() +" Post Employee Details tc started");
        String expname = "ApiAutoTest" + RestUtil.GenerateRandomNumber(3);
        String salary = RestUtil.GenerateRandomNumber(4);
        String age = RestUtil.GenerateRandomNumber(2);
        String namepath = "data.name";

        endpoint = basePath + Endpoints.CREATE_EMPLOYEE;
        System.out.println("endpoint is --> " + endpoint );
        ExtentTestReporter.getTest().log(Status.INFO,"Base URI is : " + baseURI);

        employeeDetails empdetails = new employeeDetails(expname, salary, age);
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(empdetails)
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(Endpoints.STATUSCODE).extract().response();
        ExtentTestReporter.getTest().log(Status.INFO,"Post request is executed Successfully" );
        logger.info(Thread.currentThread().getName() +" Post request is executed Successfully");
        System.out.println(response.getBody().asString());
        String actname = getStringResponse(response,namepath);
        String actstatus = getStringResponse(response,"status");
        empid = getStringResponse(response,"data.id");

        System.out.println("employee id is : " + empid);
        ExtentTestReporter.getTest().log(Status.INFO,"Response Body Status is : " + actstatus);

        Assert.assertEquals(actstatus,"success");
        Assert.assertEquals(actname,expname);

        ExtentTestReporter.getTest().log(Status.INFO,"Employee details created Successfully");
        logger.info(Thread.currentThread().getName() +"Post Employee details created Successfully");
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.MINOR)
    @Description("Test Case description: Returns Employee details Based on search ID")
    @Story("Story name:to check if specified endpoint fetches employee details Based on search ID mentioned")
    public void getEmployeeIdDetails(){
        logger.info(Thread.currentThread().getName() +" Get Employee Details based on ID search tc started");
        endpoint = Endpoints.EMPLOYEE_ID;
        queryParam = "";
        JsonPath getEmpDetails = RestUtil.getRequest(baseURI, basePath, endpoint);
        ExtentTestReporter.getTest().log(Status.INFO,"Get request is executed Successfully" );
        logger.info(Thread.currentThread().getName() +" Get request is executed Successfully");
        String status = getEmpDetails
                .getString("status");
        String actempname = getEmpDetails.getString("data.employee_name");
        Assert.assertNotNull(actempname);

        Assert.assertEquals("success",status);
        ExtentTestReporter.getTest().log(Status.INFO,"Test Passed Successfully");
        logger.info(Thread.currentThread().getName() +" Employee details Searched Successfully");
    }

   @Test(priority = 3, dependsOnMethods = "postEmployeeDetails")
   @Severity(SeverityLevel.MINOR)
   @Description("Test Case description: Update Employee details")
   @Story("Story name:to check if specified endpoint update employee details")
    public void updateEmployeeDetails(){
       logger.info(Thread.currentThread().getName() +" Update Employee Details tc started");
        String expname = "ApiAutoTest" + RestUtil.GenerateRandomNumber(3);
        String salary = RestUtil.GenerateRandomNumber(4);
        String age = RestUtil.GenerateRandomNumber(2);
        String namepath = "data.name";

        String endpoint = basePath + Endpoints.UPDATE_EMPLOYEE + empid;
        System.out.println("Update employee end point is : " + endpoint);

        employeeDetails empdetails = new employeeDetails(expname, salary, age);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(empdetails)
                .put(endpoint)
                .then()
                .assertThat()
                .statusCode(Endpoints.STATUSCODE).extract().response();
        ExtentTestReporter.getTest().log(Status.INFO,"Put request is executed Successfully" );
       logger.info(Thread.currentThread().getName() +" Update request is executed Successfully");
        System.out.println("Response Body is : " + response.getBody().asString());

        String actname = getStringResponse(response,namepath);
        String actstatus = getStringResponse(response,"status");
//        empid = getStringResponse(response,"id");
        ExtentTestReporter.getTest().log(Status.INFO,"Response Body Status is : " + actstatus);
        Assert.assertEquals(actstatus,"success");
//        Assert.assertEquals(actname,expname);

        ExtentTestReporter.getTest().log(Status.INFO,"Employee details updated Successfully");
       logger.info(Thread.currentThread().getName() +" Employee details Updated Successfully");
    }


   @Test(priority = 4, dependsOnMethods = "updateEmployeeDetails")
   @Severity(SeverityLevel.MINOR)
   @Description("Test Case description: Deletes Employee details")
   @Story("Story name:to check if specified endpoint delete employee details")
    public void deleteEmployeeRecord(){
       logger.info(Thread.currentThread().getName() +" Delete Employee Details tc started");
        String endpoint = basePath + Endpoints.DELETE_EMPLOYEE + empid;
        System.out.println("endpoint is : " + endpoint);

        String message = deleteJsonPath(endpoint, "")
                .getString("status");
        ExtentTestReporter.getTest().log(Status.INFO,"Delete request is executed Successfully" );
       logger.info(Thread.currentThread().getName() +" Delete request is executed Successfully");
        Assert.assertNotNull(message);
//        System.out.println(message);
        ExtentTestReporter.getTest().log(Status.INFO,"Employee details deleted Successfully");
       logger.info(Thread.currentThread().getName() +" Employee details Deleted Successfully");
    }

    @AfterClass
    public void afterTest() {
        //Reset Values
        RestUtil.resetBaseURI();
        RestUtil.resetBasePath();
        logger.info(Thread.currentThread().getName() +" *************** Execution Completed ********************");
    }

}