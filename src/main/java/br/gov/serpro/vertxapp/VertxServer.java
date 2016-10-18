package br.gov.serpro.vertxapp;

import br.gov.serpro.vertxapp.model.Bookmark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VertxServer extends AbstractVerticle {

    private final Map<Integer, Bookmark> BOOKMARKS = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();


    private Router router;

    public VertxServer() {
        BOOKMARKS.put(1, new Bookmark(1, "RocketChat", "http://chat.dev.sdr.serpro"));
    }


    @Override
    public void start(Future<Void> fut) throws Exception {
        setupRouters();

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
                .setConfig(
                        new JsonObject()
                                .put("http.port", port));

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VertxServer(), options);


    }

    public void setupRouters() {
        router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        // fazer o bookmark armazenando os registro em memória - HASHMAP

        // GET /bookmarks
        // POST /bookmarks

        // GET /bookmarks/:id
        // PUT /bookmarks/:id
        // DELETE /bookmarks/:id

//        router.route().handler(routingContext -> {
//
//            String header = routingContext.request().getHeader("Token");
//
//            if (!"senhasecreta".equals(header)) {
//                routingContext.fail(403);
//            } else {
//                routingContext.next();
//            }
//
//        });

        router.route(HttpMethod.GET, "/bookmarks").handler(routingContext -> {

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            // Write to the response and end it
            try {
                response.end(mapper.writeValueAsString(BOOKMARKS.values()));
            } catch (JsonProcessingException e) {
                routingContext.fail(500);
            }

        });

        router.route(HttpMethod.GET, "/bookmarks/:id").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            String idParam = routingContext.pathParam("id");

            System.out.println("CRIS SILVA DE OLIVEIRA");
            Bookmark book = BOOKMARKS.get(Integer.parseInt(idParam));
            if (book != null) {
                try {
                    response.end(mapper.writeValueAsString(book));
                } catch (JsonProcessingException e) {
                    routingContext.fail(500);
                }

            } else {

                routingContext.fail(404);
            }

        });

        router.route(HttpMethod.POST, "/bookmarks").handler(routingContext -> {

            // This handler will be called for every request
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            String bodyAsString = routingContext.getBodyAsString();

            try {
                Bookmark bookmark = mapper.readValue(bodyAsString, Bookmark.class);
                Integer id = bookmark.getId();

                if (BOOKMARKS.containsKey(id)) {
                    routingContext.fail(422);
                } else {
                    BOOKMARKS.put(id, bookmark);
                }

            } catch (IOException e) {
                routingContext.fail(400);
            }
            routingContext.response().setStatusCode(201).end("");
        });

        router.route(HttpMethod.PUT, "/bookmarks/:id").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            Integer idParam = Integer.valueOf(routingContext.pathParam("id"));


            String bodyAsString = routingContext.getBodyAsString();

            try {
                Bookmark bookmark = mapper.readValue(bodyAsString, Bookmark.class);
                Integer id = bookmark.getId();

                if (BOOKMARKS.containsKey(idParam)) {
                    if (!idParam.equals(bookmark.getId())) {
                        routingContext.fail(422);
                    }
                    BOOKMARKS.put(id, bookmark);

                    routingContext.response().setStatusCode(200).end("");

                } else {
                    routingContext.fail(404);
                }

            } catch (IOException e) {
                routingContext.fail(400);
            }

        });

        router.route(HttpMethod.DELETE, "/bookmarks/:id").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");

            String idParam = routingContext.pathParam("id");

            try {
                Integer id = Integer.parseInt(idParam);
                if (BOOKMARKS.containsKey(id)) {
                    BOOKMARKS.remove(id);
                } else {
                    routingContext.fail(404);
                }
            } catch (NumberFormatException e) {
                routingContext.fail(400);
            }

            routingContext.response().setStatusCode(204).end("");
        });
    }
}