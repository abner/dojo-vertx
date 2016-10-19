package br.gov.serpro.vertx.vertxapp.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

public abstract class EndpointBase<T,K> implements Handler<RoutingContext> {

    public final ObjectMapper mapper = new ObjectMapper();

    public abstract String getCaminho();

    public abstract K mapIdentificador(T object);

    public T readBody(RoutingContext routingContext) throws IOException {
        return this.mapper.readValue(routingContext.getBodyAsString(), this.type);
    }

    private  Class<T> type;

    public EndpointBase(Class<T> klass) {
        this.type = klass;
    }

    @Override
    public final void handle(RoutingContext routingContext) {
        // colocar aqui body handler

        // setup de default content-type

        //
        switch (routingContext.request().method()) {
            case GET:
                this.handleGET(routingContext);
                break;
            case POST:
                this.handlePOST(routingContext);
                break;
            case DELETE:
                this.handleDELETE(routingContext);
                break;
            case PUT:
                this.handlePUT(routingContext);
                break;
        }
    }

    public abstract void handleGET(RoutingContext routingContext);
    public abstract void handlePOST(RoutingContext routingContext);
    public abstract void handleDELETE(RoutingContext routingContext);
    public abstract void handlePUT(RoutingContext routingContext);
} 