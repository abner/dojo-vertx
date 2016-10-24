
declare var _: _.LoDashStatic;

namespace SchemaInspector.Bookmark {
    export var schema = {
        type: 'object',
        properties: {
            bookmark: {
                type: 'object',
                bookmark: true,
                required: [ 'id', 'nome', 'url'],
                properties: {
                    id: {
                            type: 'number'
                    },
                    nome: {
                        type: 'string'
                    },
                    url: {
                        type: 'string'
                    }
                },
            }
        }
    }


    export const validators = [
        {
            name: 'bookmark',
            fn: function (sch, parentSchema) {
                    var bookmarkValidator = function (data) {
                    if (data.id > 0) {
                        return true;
                    } else {
                        (<any>bookmarkValidator).errors = [{ keyword: 'bookmark', message: 'Bookmark id should be greater than 0' }];
                    }
                }
                return bookmarkValidator;
            },
            metaSchema: {
                type: 'boolean'
            }
        }
    ];
}
