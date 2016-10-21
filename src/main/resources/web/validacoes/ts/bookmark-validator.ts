module.exports = function (sch, parentSchema) {
    var bookmarkValidator = function (data) {
        if (data.id > 0) {
            return true;
        } else {
            (<any>bookmarkValidator).errors = [{ keyword: 'bookmark', message: 'Bookmark id should be greater than 0' }];
        }
    }
    return bookmarkValidator;
}