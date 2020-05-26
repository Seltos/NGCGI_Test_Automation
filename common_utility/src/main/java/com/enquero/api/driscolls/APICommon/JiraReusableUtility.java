package com.enquero.api.driscolls.APICommon;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class JiraReusableUtility {

    public static String createIssue(){
        PojoUtility.Fields fields = new PojoUtility.Fields();
        PojoUtility.Project project =  new PojoUtility.Project();
        project.setId(Properties.JIRA_PROJECT_ID);

        PojoUtility.Issuetype issuetype =  new PojoUtility.Issuetype();
        issuetype.setId(Properties.JIRA_ISSUE_TYPE);

        fields.setProject(project);
        fields.setSummary("Demo Check - Post Employee Enpoint failed");
        fields.setDescription("Creating of an issue using IDs for projects and issue types using the REST API");
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