
let bookmarkSchema = {
    type: 'object',
    properties: {
        bookmark: {
            type: 'object',
            bookmark: true,
            required: [ 'id', 'name', 'url'],
            properties: {
                id: {
                        type: 'number'
                },
                name: {
                    type: 'string'
                },
                url: {
                    type: 'string'
                }
            },
        }
    }
}

module.exports = bookmarkSchema;