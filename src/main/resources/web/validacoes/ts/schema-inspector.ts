let inspector = require('schema-inspector');

import * as _ from 'lodash';
import requiredValidator from './custom-validators/required.validator';
import { CustomValidator } from './models/custom-validator';
import { SchemaTypes } from './models/schema-types';

export class SchemaInspector {
  custom: SchemaTypes = {};

  constructor() {
    this.registerCustom('required', requiredValidator);
    this.registerCustom('type', function(schema, candidate) {
      debugger;
      let type: string = schema['$type'];
      let result = inspector.validate({ type: type }, candidate);
      if (!result.valid)
    			return this.report(result.format(), type);
    });
  }


  registerCustom(name: string, customFn: CustomValidator) {
    let customValidator = {
    }
    customValidator[name] = customFn;
    inspector.Validation.extend(customValidator)
  }

  validate(schema: Object, data: Object) {
    let result = inspector.validate(this.convertSchema(schema), data);
    return result;
  }

  private convertSchema(schema: Object): Object {
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
