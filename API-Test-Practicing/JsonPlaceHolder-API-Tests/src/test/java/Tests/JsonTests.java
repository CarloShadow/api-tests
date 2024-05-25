package Tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

public class JsonTests {
		
	
	@Test
	public void deveValidarUmUsuario() {
		given()
		.when()
			.get("https://jsonplaceholder.typicode.com/users/1")
		.then()
		.log().all()
		.statusCode(200)
		.body("username", is("Bret"))
		.body("address.zipcode", is("92998-3874"))
		.body("company.bs", is("harness real-time e-markets"))
		;
	}
    
	
	//Explorando o findAll
	@Test
	public void deveValidarListaUsuario() {
		given()
		.when()
			.get("https://jsonplaceholder.typicode.com/users")
		.then()
		.log().all()
		.statusCode(200)
		.body("findAll{it.username == 'Bret'}.name", hasItem("Leanne Graham"))
		.body("findAll{it.address.geo.lat < '50.00f' && it.address.geo.lng > '100.00f'}.id", hasItems(1, 5, 6, 7, 10))
		;
	}

	@Test
	public void deveUsarPost() {
		given()
		.body(("{\"title\": \"Hello universe!\"}"))
		.body(("{\"body\": \"As far as I know, I know nothing\"}"))
		.body(("{\"userId\": 7}"))
		.contentType("application/json")
		.when()
			.post("https://jsonplaceholder.typicode.com/posts")
		.then()
		.log().all()
		.statusCode(201)
		;
	}
	
}

