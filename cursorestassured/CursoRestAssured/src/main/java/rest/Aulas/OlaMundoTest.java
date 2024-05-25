package rest.Aulas;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlamundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		assertTrue(response.statusCode() == 200);
		assertEquals(200, response.statusCode());

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void outrasFormasDeRestAssured() {

		given()
		 .when()
		 	.get("http://restapi.wcaquino.me/ola")
		 .then()
		 	.statusCode(200);

	}
	
	@Test
	public void devoValidarOBody() {
		
		given()
		 .when()
		 	.get("http://restapi.wcaquino.me/ola")
		 .then()
		 	.statusCode(200)
		 	.body(is("Ola Mundo!"))
		 	.body(containsString("Ola"))
		 	.body(is(notNullValue()));
	}
}
