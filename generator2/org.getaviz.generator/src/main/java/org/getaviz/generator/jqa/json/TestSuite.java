package org.getaviz.generator.jqa.json;

import org.neo4j.driver.v1.types.Node;

public class TestSuite {
    private String type = "testSuite";
    private String id;
    private int errorCount;
    private int failureCount;
    private int skippedCount;
    private int testCount;
    private double time;

    public TestSuite(Node classNode, Node testCaseNode) {
        id = classNode.get("hash").asString();
        errorCount = testCaseNode.get("errors").asInt();
        failureCount = testCaseNode.get("failures").asInt();
        skippedCount = testCaseNode.get("skipped").asInt();
        testCount = testCaseNode.get("tests").asInt();
        time = testCaseNode.get("time").asDouble();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int count) {
        this.errorCount = count;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int count) {
        this.failureCount = count;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int count) {
        this.skippedCount = count;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int count) {
        testCount = count;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}

