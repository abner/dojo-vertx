import { CustomValidator } from './models/custom-validator';
import { SchemaTypes } from './models/schema-types';

export class SchemaInspector {
  custom: SchemaTypes = {};

  constructor(private _: any, private inspector: any) {

  }


  registerCustom(name: string, customFn: CustomValidator) {
    let customValidator = {
    }
    customValidator[name] = customFn;
    this.inspector.Validation.extend(customValidator)
  }

  validate(schema: Object, data: Object) {
    let result = this.inspector.validate(this.convertSchema(schema), data);
    return result;
  }

  private convertSchema(schema: Object): Object {
    let _ = this._;
    let _schema = {'properties': {}};
    if(schema['type']) {
      _schema['type'] = schema['type']
    }
    _.each(Object.getOwnPropertyNames(schema['properties']), (key) => {
      _schema['properties'][key] = this.transformToInternalValidators(schema['properties'][key]);
    });
    return _schema;
  }

  private transformToInternalValidators(schema: Object): Object {
    let _ = this._;
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
