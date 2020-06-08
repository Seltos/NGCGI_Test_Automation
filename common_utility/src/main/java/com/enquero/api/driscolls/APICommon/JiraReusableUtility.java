package com.enquero.api.driscolls.APICommon;


import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class JiraReusableUtility {

    public static String createIssue(String TestName){
        PojoUtility.Fields fields = new PojoUtility.Fields();
        PojoUtility.Project project =  new PojoUtility.Project();
        project.setId(Properties.JIRA_PROJECT_ID);

        PojoUtility.Issuetype issuetype =  new PojoUtility.Issuetype();
        issuetype.setId(Properties.JIRA_ISSUE_TYPE);

        fields.setProject(project);

        System.out.println("Test case name for bug creation"+TestName);
        fields.setSummary("Test " + TestName + " is failed");
        fields.setDescription(TestName + " is failed");
        fields.setIssuetype(issuetype);

        PojoUtility.Root root = new PojoUtility.Root();
        root.setFields(fields);


        Response response =  given().auth().preemptive().basic(Properties.USERNAME, Properties.PASSWORD)
                .body(root)
                .contentType(ContentType.JSON)
                .when()
                .post(Properties.JIRA_CREATEISSUE_ENDPOINT)
                .then()
                .statusCode(201).extract().response();

        String bugId = response.jsonPath().getString("key");
        return bugId;
    }
}