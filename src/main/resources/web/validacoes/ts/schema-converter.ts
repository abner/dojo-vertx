import * as _ from 'lodash';
export class SchemaConverter {

  public static convert(schema: Object): Object {
    let _schema = {'properties': {}};
    if(schema['type']) {
      _schema['type'] = schema['type']
    }
    _.each(Object.getOwnPropertyNames(schema['properties']), (key) => {
      debugger;
      _schema['properties'][key] = SchemaConverter.transformToInternalValidators(schema['properties'][key]);
    });
    return _schema;
  }

  private static transformToInternalValidators(schema: Object): Object {
    let newSchema = {};
    _.forOwn(schema, function (value, key) {

      if (['required', 'type'].indexOf(key) > -1) {
        newSchema['$' + key] = value;
      } else {
        newSchema[key] = value;
      }
    });
    return newSchema;
  }

}