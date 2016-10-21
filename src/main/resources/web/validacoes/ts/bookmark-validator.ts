
let inspector = require('schema-inspector');

module.exports = function(schema: Object, candidate: Object): void {
    console.log('HERE', this['origin'], candidate);
    if (candidate.id <=  0) {
        this.report('bookmark id should be greater than 0', 'invalid-code');
    }
}