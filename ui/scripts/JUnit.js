var junit = (function() {
    let tests = new Map();

    function initialize(testEntities) {
        testEntities.forEach(function(e) {
            tests.set(e.id, e);
        });
    }

    function getTestById(id) {
        return tests.get(id);
    }

    return {
        initialize: initialize,
        getTestById: getTestById,
    };
})();
