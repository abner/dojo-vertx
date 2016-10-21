package br.gov.serpro.vertx.vertxapp.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by 80129498572 on 21/10/16.
 */
public class SchemaInspectorTest {

    private SchemaInspector schemaInspector;
    private ObjectMapper mapper = new ObjectMapper();
    @Before
    public void before() throws Exception {
        schemaInspector = new SchemaInspector();
    }

    @Test
    public void validatesUsingJsonSchema() throws IOException {
        for(int i = 0; i < 25; i++) {
            Object object = mapper.readValue("{ \"bookmark\": { \"id\": 1 , \"name\": \"ABNER\"}}", Object.class);
            Object result = schemaInspector.validate("bookmark", object);
            String jsonResult = mapper.writeValueAsString(result);
            System.out.println("RESULT: " + jsonResult);
        }
    }


}
