package br.gov.serpro.vertx.vertxapp.rest.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.gov.serpro.vertx.vertxapp.models.Bookmark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class BookmarkHandler implements Handler<RoutingContext> {

    private Map<Integer, Bookmark> BOOKMARKS = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    public BookmarkHandler(Map<Integer, Bookmark> bookmarks) {
        this.BOOKMARKS = bookmarks;
    }


    @Override
    public void handle(RoutingContext routingContext) {
        switch (routingContext.request().method()) {
            case GET:
                handleGET(routingContext);
                break;
            case POST:
                handlePOST(routingContext);
                break;
            case PUT:
                handlePUT(routingContext);
                break;
            case DELETE:
                handleDELETE(routingContext);
                break;
            default:
                routingContext.fail(500);
                break;
        }

    }


    private void handleGET(RoutingContext routingContext) {
        if (routingContext.pathParams().size() > 0) {
            handleGetOne(routingContext);
        } else {
            handleGetAll(routingContext);
        }
    }

    private void handlePOST(RoutingContext routingContext) {
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
                routingContext.response().setStatusCode(201).end("");
            }
        } catch (IOException e) {
            routingContext.fail(400);
        }
    }

    private void handlePUT(RoutingContext routingContext) {
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
                    return;
                }
                BOOKMARKS.put(id, bookmark);

                routingContext.response().setStatusCode(200).end("");

            } else {
                routingContext.fail(404);
            }

        } catch (IOException e) {
            routingContext.fail(400);
        }
    }

    private void handleDELETE(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");

        String idParam = routingContext.pathParam("id");

        try {
            Integer id = Integer.parseInt(idParam);
            if (BOOKMARKS.containsKey(id)) {
                BOOKMARKS.remove(id);
                routingContext.response().setStatusCode(204).end("");
            } else {
                routingContext.fail(404);
            }
        } catch (NumberFormatException e) {
            routingContext.fail(400);
        }
    }

    private void handleGetAll(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");
        try {
            response.end(mapper.writeValueAsString(BOOKMARKS.values()));
        } catch (JsonProcessingException e) {
            routingContext.fail(500);
        }
    }

    private void handleGetOne(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");

        String idParam;
        try {
            idParam = routingContext.pathParam("id");
        } catch (Exception e1) {
            routingContext.fail(400);
            return;
        }

        Integer idInteger;
        try {
            idInteger = Integer.parseInt(idParam);
        } catch (NumberFormatException e1) {
            routingContext.fail(400);
            return;
        }

        Bookmark book = BOOKMARKS.get(idInteger);
        if (book != null) {
            try {
                response.end(mapper.writeValueAsString(book));
            } catch (JsonProcessingException e) {
                routingContext.fail(500);
            }

        } else {

            routingContext.fail(404);
        }
    }


}
