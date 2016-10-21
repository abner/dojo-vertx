
let bookmarkSchema = {

    $bookmark: true,
    properties: {
        bookmark: {
            $bookmark: true,
            properties: {
                id: {
                    type: 'number'
                },
                name: {
                    type: 'string',
                    required: true
                },
                url: {
                    type: 'string',
                    required: true
                }
            }
        }
    }
}

module.exports = bookmarkSchema;