import java.util.HashMap;
import java.util.Map;

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
		default:
			break;
		}
		
	}
	
	
	public void handleGET(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");
		try {
			response.end(mapper.writeValueAsString(BOOKMARKS.values()));
		} catch (JsonProcessingException e) {
			routingContext.fail(500);
		}
	}
	
	

}
