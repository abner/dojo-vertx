s = require('./js/schema-inspector');
bs = require('./js/bookmark-schema');
bv = require('./js/bookmark-validator');

console.log('BOOKARM VALIDATOR', bv);
s1 = new s.SchemaInspector();
s1.registerCustom('bookmark', bv);

console.log('SCHEMA', bs);

let result = s1.validate(bs, { bookmark: { id: 0 }});

console.log('RESULT', result);
