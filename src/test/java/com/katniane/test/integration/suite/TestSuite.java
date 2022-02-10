package com.katniane.test.integration.suite;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
	}
}
