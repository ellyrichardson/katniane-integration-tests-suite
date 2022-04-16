package com.katniane.test.integration.suite;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class TestSuite {
	
	public static void getResponseBody() {
		// Specify the base URL to the RESTful web service 
		RestAssured.baseURI = "127.0.0.1:3030/v1"; 
		// Get the RequestSpecification of the request to be sent to the server. 
		RequestSpecification httpRequest = RestAssured.given(); 
		// specify the method type (GET) and the parameters if any. 
		//In this case the request does not take any parameters 
		Response response = httpRequest.request(Method.GET, "");
		// Print the status and message body of the response received from the server 
		System.out.println("Status received => " + response.getStatusLine()); 
		System.out.println("Response=>" + response.prettyPrint());
		
	}
	
	@Test
	public void testPost() {
		RestAssured.baseURI = "http://127.0.0.1:3030/v1"; 
		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject(); 
		try {
			requestParams.put("filename", "db_login_attempts.log");
			requestParams.put("title", "DB login attempt"); 
			requestParams.put("content", "Richard has 3 failed attempts with logging in to the DB"); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Add a header stating the Request body is a JSON 
		request.header("Content-Type", "application/json"); // Add the Json to the body of the request 
		request.body(requestParams.toString()); // Post the request and check the response
		
		Response response = request.post("/logs"); 
		System.out.println("The status received: " + response.statusLine());
		System.out.println("Response=>" + response.prettyPrint());
		
		Response getResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/logs/db_login_attempts.log/2022-04-16")
                .then()
                .extract().response();
		
		Assertions.assertEquals(200, getResponse.statusCode());
        Assertions.assertEquals("DB login attempt", getResponse.jsonPath().getString("contents.title[0]"));
        Assertions.assertEquals("Richard has 3 failed attempts with logging in to the DB", getResponse.jsonPath().getString("contents.content[0]"));
        Assertions.assertFalse(getResponse.jsonPath().getString("contents.timestamp[0]").isEmpty());
        Assertions.assertFalse(getResponse.jsonPath().getString("contents.reporter[0]").isEmpty());
        System.out.println("The getResponse status received: " + getResponse.statusLine());
		System.out.println("getResponse=>" + getResponse.prettyPrint());
	}
	
	@Test
	public void testLogOpening() {
		/*
		 * Save an audit log
		 * */
		RestAssured.baseURI = "http://127.0.0.1:3030/v1"; 
		RequestSpecification request = RestAssured.given();
		JSONObject requestParams = new JSONObject(); 
		try {
			requestParams.put("filename", "some_log_to_be_claimed.log");
			requestParams.put("title", "Log Claiming Attempt"); 
			requestParams.put("content", "Richard has 3 failed attempts when claiming the log"); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Add a header stating the Request body is a JSON 
		request.header("Content-Type", "application/json"); // Add the Json to the body of the request 
		request.body(requestParams.toString()); // Post the request and check the response
		
		Response response = request.post("/logs"); 
		System.out.println("The status received: " + response.statusLine());
		System.out.println("Response=>" + response.prettyPrint());
		
		Response getResponse = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/logs/some_log_to_be_claimed.log/2022-04-16")
                .then()
                .extract().response();
		
		Assertions.assertEquals(200, getResponse.statusCode());
        Assertions.assertEquals("Log Claiming Attempt", getResponse.jsonPath().getString("contents.title[0]"));
        Assertions.assertEquals("Richard has 3 failed attempts when claiming the log", getResponse.jsonPath().getString("contents.content[0]"));
        Assertions.assertFalse(getResponse.jsonPath().getString("contents.timestamp[0]").isEmpty());
        Assertions.assertFalse(getResponse.jsonPath().getString("contents.reporter[0]").isEmpty());
        System.out.println("The getResponse status received: " + getResponse.statusLine());
		System.out.println("getResponse=>" + getResponse.prettyPrint());
		
		/*
		 * Open the added log for claiming
		 * */
		JSONObject requestParamsForOpeningLog = new JSONObject(); 
		try {
			requestParamsForOpeningLog.put("filename", "some_log_to_be_claimed.log");
			// Charlie's pubkey
			requestParamsForOpeningLog.put("claimer_pubkey", "5FLSigC9HGRKVhB9FiEo4Y3koPsNmBmLJbpXg2mp1hXcS59Y"); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Add a header stating the Request body is a JSON 
		request.body(requestParamsForOpeningLog.toString()); // Post the request and check the response
		
		Response logOpeningResponse = request.post("/log_ownership/open"); 
		System.out.println("The status received: " + logOpeningResponse.statusLine());
		System.out.println("Response=>" + logOpeningResponse.prettyPrint());
	}
	
	/*
	 * NOTE: This test must be run by the actual claimer, meaning this will not work if the claimer is also the opener
	 * */
	@Test
	public void testLogClaiming() {
		RestAssured.baseURI = "http://127.0.0.1:3030/v1"; 
		/*
		 * Claim an open log
		 * */
		JSONObject requestParamsForClaimingLog = new JSONObject(); 
		try {
			requestParamsForClaimingLog.put("filename", "some_log_to_be_claimed.log");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		RequestSpecification request = RestAssured.given();
		// Add a header stating the Request body is a JSON 
		request.header("Content-Type", "application/json"); // Add the Json to the body of the request 
		request.body(requestParamsForClaimingLog.toString()); // Post the request and check the response
		
		Response logClaimingResponse = request.post("/log_ownership/claim"); 
		System.out.println("The status received: " + logClaimingResponse.statusLine());
		System.out.println("Response=>" + logClaimingResponse.prettyPrint());
	}
}
