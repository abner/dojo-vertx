import { CustomValidator } from './../models/custom-validator';


export default function requiredValidator(schema, candidate) {
  let required: boolean = schema['required'];
  if (required && (candidate === undefined || candidate == null)) {
    this.report('is required', 'required');
  }
}

