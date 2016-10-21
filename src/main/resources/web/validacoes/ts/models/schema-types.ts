import { CustomValidator } from './custom-validator';


export interface SchemaTypes {
  [name: string]: CustomValidator;
}
