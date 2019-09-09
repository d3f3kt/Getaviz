var junitInfoController = (function () {

    let controllerConfig = {};

    function initialize(setupConfig) {
        application.transferConfigParams(setupConfig, controllerConfig);
    }

    function activate(rootDiv) {
        let container = document.createElement("DIV");
        container.id = "junitViewDiv";
        container.setAttribute("class", "grid-container");

        rootDiv.appendChild(container);

        events.selected.on.subscribe(onEntitySelect);

    }

    function displayTestInfo(testEntity) {
        let rootContainer = document.getElementById("junitViewDiv");

        if (testEntity.type === "testSuite") {
            displayTestSuite(testEntity, rootContainer);
        }

        if (testEntity.type === "testCase") {
            displayTestCase(testEntity, rootContainer);
        }
    }

    function displayTestSuite(testSuite, rootContainer) {
        appendRow("Test count", testSuite.testCount, rootContainer);
        appendRow("Test with errors", testSuite.errorCount, rootContainer);
        appendRow("Test with failures", testSuite.failureCount, rootContainer);
        appendRow("Skipped tests", testSuite.skippedCount, rootContainer);
        appendRow("Time",
            Math.round(testSuite.time * 10 ** 5) / 10 ** 5
            , rootContainer);
    }

    function displayTestCase(testCase, rootContainer) {
        appendRow("Result", testCase.result, rootContainer);
        appendRow("Time", Math.round(testCase.time * 10 ** 5) / 10 ** 5, rootContainer);
    }

    function appendRow(name, data, rootContainer) {
        let leftCell = document.createElement("div");
        let rightCell = document.createElement("div");
        leftCell.setAttribute("class", "grid-item-left");
        rightCell.setAttribute("class", "grid-item-right");

        let nameElement1 = document.createTextNode(name);
        let nameElement2 = document.createTextNode(data);
        leftCell.appendChild(nameElement1);
        rightCell.appendChild(nameElement2);

        rootContainer.appendChild(leftCell);
        rootContainer.appendChild(rightCell);
    }

    function onEntitySelect(applicationEvent) {
        const entity = applicationEvent.entities[0];
        const testEntity = junit.getTestById(entity.id);

        resetView();

        if (testEntity) {
            displayTestInfo(testEntity);
        }
    }

    function reset() {
    }

    function resetView() {
        let div = document.getElementById("junitViewDiv");
        div.innerHTML = "";
    }

    return {
        initialize: initialize,
        activate: activate,
        reset: reset
    };
})();
