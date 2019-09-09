package org.getaviz.generator.jqa.json;

import org.neo4j.driver.v1.types.Node;

public class TestCase {
    private String type = "testCase";

    private String id;
    private String classId;
    private String result;
    private double time;

    public TestCase(Node methodNode, Node classNode, Node testCaseNode) {
        id = methodNode.get("hash").asString();
        classId = classNode.get("hash").asString();
        result = testCaseNode.get("result").asString();
        time = testCaseNode.get("time").asDouble();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String id) {
        classId = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
