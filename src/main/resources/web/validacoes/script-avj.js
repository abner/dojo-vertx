
/** Evita inicializar a engine de validação toda hora */
if (!loaded) {
    ajv = new Ajv({ allErrors: true, verbose: false });
    lodaded = true;
}

// obtem o schema
var schema = SchemaInspector[_.capitalize(schemaName)].schema;

// adiciona os validadores do schema
for(i=0; i< SchemaInspector[_.capitalize(schemaName)].validators.lenght; i++) {
    ajv.addKeyword(validators[i].name, {
                                            type: 'object',
                                            compile: validators[i].fn,
                                            error: 'full',
                                            metaSchema: validators[i].metaSchema
                                        }
    );
}


// compila o esquema de validação, retornando a função de validação
var validate = ajv.compile(schema);

/* constroi objeto na estrutura: 
        { 
            `schemaName`: {}
        }
*/
var obj = {};
obj[schemaName] = value;

// executa a validação
var result = validate(obj);

// se o resultado da validação for false, então pega os erros gerados como o resultado
if(!result) {
    result = validate.errors;
}
