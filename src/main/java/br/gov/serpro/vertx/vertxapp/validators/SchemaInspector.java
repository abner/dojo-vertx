package br.gov.serpro.vertx.vertxapp.validators;

import com.coveo.nashorn_modules.Require;
import com.coveo.nashorn_modules.ResourceFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class provides a interface to run validations thorugh the ajv node-js json schema validator
 * The idea is to have the same mechanism we have on frontend, so validation code will be shared
 * between the the application layers (backend / frontend)
 *
 * Important: In a multithread environment this class should not be shared between multiple threads
 * because of the javascript mono-thread nature we need to have a pool of SchemaInspector instances
 * to avoid race conditions
 *
 * Created by 80129498572 on 21/10/16.
 */
public class SchemaInspector {

    private final static String AJV_SCRIPT_NAME = "ajv.min.js";
    private final static String LODASH_SCRIPT_NAME = "lodash.custom.min.js";

    private final static String JAVASCRIPT_ROOT_FOLDER = "/web/validacoes";
    private final static String VALIDATOR_RUNNER_SCRIPT_NAME = "script-avj.js";


    private ObjectMapper mapper = new ObjectMapper();

    private NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

    // private URL AJV_SCRIPT_URL = SchemaInspector.class.getClassLoader().getResource("web/validacoes/script-avj.js");
    public SchemaInspector() throws  RuntimeException {
        prepare();
    }


    protected void prepare()  {

//        ResourceFolder rootFolder = ResourceFolder.create(SchemaInspector.class.getClassLoader(), JAVASCRIPT_ROOT_FOLDER, "UTF-8");
//        try {
//            Require.enable(nashorn, rootFolder);
//        } catch (ScriptException e) {
//            throw new RuntimeException("Schema Inspector failed to setup require function");
//        }
        nashorn.put("loaded", false);
    }

    public Object validate(String schemaName, Object target) {

        try {
            nashorn.put("schemaName", schemaName);

            // ugly but necessary to the object behaves like a common anonymous javascript Object
            // and so, works as it should in the browser
            nashorn.eval("var value = " +  mapper.writeValueAsString(target) + ";");

            // passa o objectMapper para o javascript para permitir print do JSON para DEBUG
            nashorn.put("mapper", mapper);

            // CARREGA SCRIPTS do AJV e do LODASH
            loadScripts(LODASH_SCRIPT_NAME);
            loadScripts(AJV_SCRIPT_NAME);

            // CARREGA O SCRIPT DE SCHEMA E CUSTOM VALIDATORS A PARTIR DO SCHEMA
            loadScripts("js/schema-and-validators/" + schemaName + ".js");

            // CARREGA O SCRIPT DE Validaçao utilizando o AVJ
            // executando o script de validaçao
            loadScripts(VALIDATOR_RUNNER_SCRIPT_NAME);

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // obtem o resultado da execuçao do script
        Object objResult = nashorn.getBindings(ScriptContext.ENGINE_SCOPE).get("result");


        return objResult;
    }

    private void loadScripts(String scriptName) throws ScriptException {
        String scriptFilePath = JAVASCRIPT_ROOT_FOLDER + "/" + scriptName;
        InputStreamReader is = new InputStreamReader(getClass().getResourceAsStream(scriptFilePath));
        //String ajvFile = AJV_SCRIPT_URL.getFile();
        System.out.println("LOADING SCRIPT FILE: " + scriptFilePath);
        nashorn.eval(is);
    }



}
