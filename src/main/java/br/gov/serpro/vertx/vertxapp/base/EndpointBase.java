package br.gov.serpro.vertx.vertxapp.base;

public abstract class EndpointBase<T,K> {


    public abstract String getCaminho();

    public abstract K mapIdentificador(T object);

    private  Class<T> type;

    public EndpointBase(Class<T> klass) {
        this.type = klass;
    }
} 