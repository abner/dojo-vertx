var  inspector = require('schema-inspector');
var  _ = require('lodash');
var SchemaInspector = require('./js/schema-inspector').SchemaInspector;
var schema = require('./js/' + schemaName + '-schema');
var validator = require('./js/' + schemaName + '-validator');

var requiredValidator = require('./js/custom-validators/required.validator');

//var schemaInspector = new SchemaInspector(_, inspector);
//
//
//schemaInspector.registerCustom('required', requiredValidator);
//schemaInspector.registerCustom('type', function(schema, candidate) {
//      var type = schema['$type'];
//      var result = inspector.validate({ type: type }, candidate);
//      if (!result.valid)
//    			return this.report(result.format(), type);
//    });
//
//schemaInspector.registerCustom('bookmark', validator);
//var result = schemaInspector.validate(schema, value);


debugger;
print('schema', mapper.writeValueAsString(schema));
print('value', mapper.writeValueAsString(value));
var obj = null

var result = inspector.validate(schema, value);

