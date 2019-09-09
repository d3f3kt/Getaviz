package org.getaviz.generator.jqa;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.getaviz.generator.SettingsConfiguration;
import org.getaviz.generator.database.DatabaseConnector;
import org.getaviz.generator.jqa.json.TestCase;
import org.getaviz.generator.jqa.json.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class JUnit2JSON {
    SettingsConfiguration config = SettingsConfiguration.getInstance();
    DatabaseConnector connector = DatabaseConnector.getInstance();

    private Gson gson;

    public JUnit2JSON() {
        gson = new Gson();
    }

    public String getJunitJson() {
        return gson.toJson(getData());
    }

    private List<Object> getData() {
        List<Object> data = new ArrayList<Object>();

        data.addAll(getClassData());
        data.addAll(getMethodData());

        return data;
    }

    private List<TestSuite> getClassData() {
        List<TestSuite> data = new ArrayList<TestSuite>();

        connector.executeRead("MATCH (c:Class)<-[:TESTS]-(t:TestSuite) return c, t")
                .forEachRemaining((result) -> {
                    data.add(new TestSuite(
                            result.get("c").asNode(),
                            result.get("t").asNode()
                    ));
                });

        return data;
    }

    private List<TestCase> getMethodData() {
        List<TestCase> data = new ArrayList<TestCase>();

        connector.executeRead("MATCH (c:Class)-[:DECLARES]->(m:Method)<-[:TESTS]-(t:TestCase) return m, t, c")
                .forEachRemaining((result) -> {
                    data.add(new TestCase(
                            result.get("m").asNode(),
                            result.get("c").asNode(),
                            result.get("t").asNode()
                            ));
                });

        return data;
    }
}
