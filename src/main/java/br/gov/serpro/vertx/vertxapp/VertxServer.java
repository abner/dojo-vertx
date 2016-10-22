package br.gov.serpro.vertx.vertxapp;

import br.gov.serpro.vertx.vertxapp.models.Bookmark;
import br.gov.serpro.vertx.vertxapp.rest.handlers.BookmarkHandler;
import br.gov.serpro.vertx.vertxapp.validators.SchemaInspector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VertxServer extends AbstractVerticle {

	private SchemaInspector inspector = new SchemaInspector();

	private final Map<Integer, Bookmark> BOOKMARKS = new HashMap<>();

	private final ObjectMapper mapper = new ObjectMapper();

	private Router router;

	public VertxServer() {
		BOOKMARKS.put(1, new Bookmark(1, "RocketChat", "http://chat.dev.sdr.serpro"));
		configurarRotas();
	}

	@Override
	public void start(Future<Void> fut) throws Exception {
		super.start();

		vertx.createHttpServer().requestHandler(router::accept).listen(
                config().getInteger("http.port", 8080),
                result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                }
        );
	}

	public static void main(String[] args) {
		Integer port = 8080;
		DeploymentOptions options = new DeploymentOptions()
				.setConfig(new JsonObject()
						.put("http.port", port));
		Vertx vertx = Vertx.vertx();
		options.setMaxWorkerExecuteTime(10000);
		vertx.deployVerticle(new VertxServer(), options);


	}

	private void configurarRotas() {
		router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		// fazer o bookmark armazenando os registro em memória - HASHMAP

		// GET /bookmarks
		// POST /bookmarks

		// GET /bookmarks/:id
		// PUT /bookmarks/:id
		// DELETE /bookmarks/:id

//		router.route().handler(routingContext -> {
//
//			String header = routingContext.request().getHeader("Token");
//
//			if (!"senhasecreta".equals(header)) {
//				routingContext.fail(403);
//			} else {
//				routingContext.next();
//			}
//
//		});
		
		BookmarkHandler bookmarkHandler = new BookmarkHandler(BOOKMARKS);

		router.route(HttpMethod.GET, "/bookmarks").handler(bookmarkHandler);
		router.route(HttpMethod.GET, "/bookmarks/:id").handler(bookmarkHandler);
		router.route(HttpMethod.POST, "/bookmarks").handler(bookmarkHandler);
		router.route(HttpMethod.PUT, "/bookmarks/:id").handler(bookmarkHandler);
		router.route(HttpMethod.DELETE, "/bookmarks/:id").handler(bookmarkHandler);

		router.route(HttpMethod.POST, "/bookmarks/validation").handler(routingContext -> {
			String json = routingContext.getBodyAsString();
			try {
				Bookmark bookmark = mapper.readValue(json, Bookmark.class);
				Object result = inspector.validate("bookmark", bookmark);
				try {
					routingContext.response().end(mapper.writeValueAsString(result));
				} catch (JsonProcessingException e) {
					routingContext.fail(e);
					return;
				}
			} catch (IOException e) {
				routingContext.fail(422);
				return;
			}

		});
	}
}