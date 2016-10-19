package br.gov.serpro.vertx.vertxapp.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by abner on 19/10/16.
 */
public class CustomMapper  extends ObjectMapper {
    public CustomMapper() {
        super();
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
