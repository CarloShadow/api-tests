package rest.Aulas;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class UserJsonTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
		
	} 
	
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("/users/1")
		.then()
			.body("id", is(1))
			.body("age", greaterThan(18))
		;
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		
		Response response = RestAssured.request(Method.GET, "/users/1");
			
		//Path
		assertEquals(new Integer (1), response.path("id"));
			
		//JsonPath
		JsonPath jPath = new JsonPath(response.asString());
		assertEquals(1, jPath.getInt("id"));
			
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
			
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		
		given()
		.when()
			.get("/users/2")
		.then()
			.body("name", containsString("Joaquina"))
			.body("endereco.numero", is(0))
			.body("salary", greaterThan(2499))
		;
			
	}
	
	@Test
	public void deveVerificarLista() {
		
		given()
		.when()
			.get("/users/3")
		.then()
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Luizinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"))
			;
			
	}
	
//	@Test
//	public void deveVerificarerrousuarioInexistente() {
//		
//		given()
//		.when()
//			.get("/users/4")
//		.then()
//			.statusCode(404)
//		
//		;
//			
//	}
	
	@Test
	public void deveVerificarListaRaiz() {
		
		given()
		.when()
			.get("/users")
		.then()
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[2]", is(20))
			.body("filhos.name", hasItems(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
		;
			
	}
	
	@Test
	public void deveFazerVericacoesAvancadas() {
		
		given()
		.when()
			.get("/users")
		.then()
			.body("$", hasSize(3))
			
			//Procura valores maiores ou iguais a 25 em todos campos age
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			
			//Usando findaAll para achar um valor especifico no campo name
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
		;
			
	}
}
