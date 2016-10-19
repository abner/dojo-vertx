import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.restassured.RestAssured;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class VertxServerTest {

	private Vertx vertx;
	

	@BeforeClass
	public static void configureRestAssured() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = Integer.getInteger("http.port", 8081);
	}

	@AfterClass
	public static void unconfigureRestAssured() {
		RestAssured.reset();
	}
	
	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", 8081));
		vertx.deployVerticle(new VertxServer(), options, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void obterListaBookmarks() {
		get("/bookmarks").then()
			.assertThat()
			.statusCode(200)
			.body("[0].id", equalTo(1))
			.body("[0].nome", equalTo("RocketChat"))
			.body("[0].url", equalTo("http://chat.dev.sdr.serpro"));
	}
	
	@Test
	public void obterBookmarkEspecifico() {
		get("/bookmarks/1").then()
			.assertThat()
			.statusCode(200)
			.body("id", equalTo(1))
			.body("nome", equalTo("RocketChat"))
			.body("url", equalTo("http://chat.dev.sdr.serpro"));
	}
	
	@Test
	public void obterBookmarkNaoExistente() {
		get("/bookmarks/2").then()
			.assertThat()
			.statusCode(404);
	}
	
	@Test
	public void obterBookmarkComIdInvalido() {
		get("/bookmarks/abc").then()
			.assertThat()
			.statusCode(400);
	}
	
	@Test
	public void incluiBookmark() {
		 given().body(new Bookmark(2, "teste", "http://teste.dev.sdr.serpro")) 
		 .when()
		 .post("/bookmarks")
		 .then()
		 .assertThat()
		 .statusCode(201);
	}

	@Test
	public void incluiBookmarkExistente() {
		 given().body(new Bookmark(1, "teste", "http://teste.dev.sdr.serpro"))
		 .when()
		 .post("/bookmarks")
		 .then()
		 .assertThat()
		 .statusCode(422);
	}

	@Test
	public void incluiBookmarkJSONInvalido() {
		 given().body("{\"idade\":1,\"qquercoisa\":\"teste\",\"url\":\"http://teste.dev.sdr.serpro\"}")
		 .when()
		 .post("/bookmarks")
		 .then()
		 .assertThat()
		 .statusCode(400);
	}
	
	@Test
	public void alterarBookmark() {
		 given().body(new Bookmark(1, "teste", "http://teste.dev.sdr.serpro"))
		 .when()
		 .put("/bookmarks/1")
		 .then()
		 .assertThat()
		 .statusCode(200);
	}
	
	@Test
	public void alterarBookmarkNaoExistente() {
		 given().body(new Bookmark(1, "teste", "http://teste.dev.sdr.serpro"))
		 .when()
		 .put("/bookmarks/2")
		 .then()
		 .assertThat()
		 .statusCode(404);
	}
	
	@Test
	public void alterarBookmarkComIdDiferente() {
		 given().body(new Bookmark(2, null, null))
		 .when()
		 .put("/bookmarks/1")
		 .then()
		 .assertThat()
		 .statusCode(422);
	}
	
	
	@Test
	public void apagarUmBookmark() {
		 delete("/bookmarks/1")
		 .then()
		 .assertThat()
		 .statusCode(204);
	}
	
	@Test
	public void apagarUmBookmarkInexistente() {
		 delete("/bookmarks/2")
		 .then()
		 .assertThat()
		 .statusCode(404);
	}
	
	@Test
	public void apagarUmBookmarkComIdValido() {
		 delete("/bookmarks/a2")
		 .then()
		 .assertThat()
		 .statusCode(400);
	}
	
}
