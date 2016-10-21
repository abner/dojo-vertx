package br.gov.serpro.vertx.vertxapp.validators;

import com.coveo.nashorn_modules.Require;
import com.coveo.nashorn_modules.ResourceFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by 80129498572 on 21/10/16.
 */
public class SchemaInspector {

    private ObjectMapper mapper = new ObjectMapper();
    private NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
    private final static String AVJ_SCRIPT_URL = "classpath:web/validacoes/script-avj.js";
    private final static String JAVASCRIPT_ROOT_FOLDER = "./web/validacoes";
    public SchemaInspector() throws  Exception {
        prepare();
    }

    protected void prepare() throws ScriptException {
        ResourceFolder rootFolder = ResourceFolder.create(SchemaInspector.class.getClassLoader(), JAVASCRIPT_ROOT_FOLDER, "UTF-8");
        Require.enable(nashorn, rootFolder);
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

            // CARREGA O SCRIPT DE Validaçao utilizando o AVJ
            // executando o script de validaçao
            nashorn.eval("load('" + AVJ_SCRIPT_URL +  "');");
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // obtem o resultado da execuçao do script
        Object objResult = nashorn.getBindings(ScriptContext.ENGINE_SCOPE).get("result");


        return objResult;
    }



}
