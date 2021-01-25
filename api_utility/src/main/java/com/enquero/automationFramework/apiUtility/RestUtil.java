package com.enquero.automationFramework.apiUtility;



import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.basePath;



public class RestUtil {
    //Global Setup Variables
    public static String path; //Rest request path
    private static Response res = null; //Response object
    private static JsonPath jp = null; //JsonPath object

    /*
    ***Sets Base URI***
    Before starting the test, we should set the RestAssured.baseURI
    */
    public static void setBaseURI (String baseURI){
        RestAssured.baseURI = baseURI;
    }

    /*
    ***Sets base path***
    Before starting the test, we should set the RestAssured.basePath
    */
    public static void setBasePath(String basePathTerm){
        RestAssured.basePath = basePathTerm;
    }

    /*
    ***Reset Base URI (after test)***
    After the test, we should reset the RestAssured.baseURI
    */
    public static void resetBaseURI (){
        RestAssured.baseURI = null;
    }

    /*
    ***Reset base path (after test)***
    After the test, we should reset the RestAssured.basePath
    */
    public static void resetBasePath(){
        RestAssured.basePath = null;
    }

    /*
    ***Sets ContentType***
    We should set content type as JSON or XML before starting the test
    */
    public static void setContentType (ContentType Type){
        given().contentType(Type);
    }

    /*
    ***search query path***
    It is  equal to "barack obama/videos.json?num_of_videos=4"
    */
    public static void  createSearchQueryPath(String endpoint)
    {
        path = baseURI+basePath+endpoint;
        System.out.println("path is : "+path);
        //path = searchTerm + "/" + jsonPathTerm + "?" + param + "=" + paramValue;
    }

    /*
    ***Returns response***
    We send "path" as a parameter to the Rest Assured'a "get" method
    and "get" method returns response of API
    */
    public static Response getResponse() {
        //System.out.print("path: " + path +"\n");
        return get(path);
    }
    public static Response postResponse( )
    {
        return RestAssured.post(path);
    }
    public static Response deleteResponse()
    {
        return RestAssured.delete(path);
    }

    /*
     ***Returns JsonPath object***
     * First convert the API's response to String type with "asString()" method.
     * Then, send this String formatted json response to the JsonPath class and return the JsonPath
     */
    public static JsonPath getJsonPath(Response r)
    {
        String resString = r.getBody().toString();
        System.out.println("res string "+resString);
        // log.info(resString);
        // JsonPath j=new JsonPath(resString);
        //JSONObject jsonObject = new JSONObject(resString);
        //JSONObject myResponse = jsonObject.getJSONObject("MyResponse");
        //JSONArray tsmresponse = (JSONArray) myResponse.get("listTsm");
        //https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
        JsonPath j = r.jsonPath();
        return  j;
    }

    public static String getStringResponse(Response res , String path){
//        System.out.println("getStringResponse is " + res.getBody().jsonPath().getString(path));

        return res.jsonPath().getString(path);

    }

    public static JsonPath deleteJsonPath(String endpoint, String queryParam){
        RestAssured.baseURI = baseURI;
        Response response = given().contentType(ContentType.JSON)
                .delete(endpoint)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .response();
        return response.jsonPath();

    }

    /*	To Generate Random Numbers of the Entered Length.*/
    public static String GenerateRandomNumber(int length)
    {
        Random rand = new Random();
        StringBuffer output = new StringBuffer(1000);
        output.append("1");
        for (int i = 1; i < length; i++)
        {
            output.append(0);
        }
        int num = Integer.parseInt(output.toString());
        int maxnum = (num*10) - 1;
        int randnum = num + rand.nextInt(maxnum) + 1;

        String RandNum = new Integer(randnum).toString();
        return RandNum;
    }

    /*	To Generate Random Capitalized Letters of the Entered Length.*/
    public static String GenerateRandomCapitalizedString(int length)
    {
        Random rand = new Random();
        StringBuffer output = new StringBuffer(1000);
        output.append((char)(rand.nextInt(26) + 'A'));
        System.out.println(output.toString());

        for(int i = 1; i < 10; i++)
        {
            char c = (char)(rand.nextInt(26) + 'a');
            output.append(c);
        }
        String RandCapitalizedString = output.toString();
        return RandCapitalizedString;
    }

    public Response getRequest(String endpoint , String queryParam){
        Response response = null;
        try {
            response = RestAssured.given().contentType(ContentType.JSON)
                    .when()
                    .get(endpoint).then().extract().response();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  response;
    }

    public static JsonPath getRequest(String baseURI, String basePath, String endpoint, String queryParam) {

        RestAssured.baseURI = baseURI;
        String getRequestResource = basePath + endpoint;
        System.out.println("Path is : " + getRequestResource);

        res = given().queryParam(queryParam)
                .contentType(ContentType.JSON)
                .when()
                .get(getRequestResource)
                .then().assertThat()
                .statusCode(200).extract().response();

        System.out.println("response is " + res);

        // System.out.println("To String ::" + res.getBody().asString());
        jp = RestUtil.getJsonPath(res);
        return jp;
    }

    public static JsonPath getRequest(String baseURI, String basePath, String endpoint) {

        RestAssured.baseURI = baseURI;
        String getRequestResource = basePath + endpoint;
        System.out.println("Path is : " + getRequestResource);

        res = given().contentType(ContentType.JSON)
                .when()
                .get(getRequestResource)
                .then().assertThat()
                .statusCode(200).extract().response();

        System.out.println("response is " + res);

        // System.out.println("To String ::" + res.getBody().asString());
        jp = RestUtil.getJsonPath(res);
        return jp;
    }

    public JsonPath postRequest(String baseURI, String basePath, String endpoint, String queryParam, String payload) {

        //PayLoad payLoad=new PayLoad();
        RestAssured.baseURI = baseURI;
        String getRequestResource = basePath + endpoint;
        res = given().queryParam(queryParam)//.body(payLoad.getPayloadLatestVersion())
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then().assertThat()
                .statusCode(201).extract().response();
        System.out.println("To String ::" + res.getBody().asString());
        jp = RestUtil.getJsonPath(res);
        return jp;
    }

}
