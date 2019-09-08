package org.getaviz.generator.jqa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.getaviz.generator.SettingsConfiguration;
import org.getaviz.generator.database.DatabaseConnector;
import org.neo4j.driver.v1.types.Node;

public class DatabaseJunitBuilder {
    Log log = LogFactory.getLog(this.getClass());
    SettingsConfiguration config = SettingsConfiguration.getInstance();
    DatabaseConnector connector = DatabaseConnector.getInstance();
    Runtime runtime = Runtime.getRuntime();

    public DatabaseJunitBuilder() {
        enhance();
    }

    public void enhance() {
        log.info("JUnit enhancement started.");

        updateFqn();
        addTestSuiteRelation();
        addTestRelation();

        log.info("JUnit enhancment finished.");
    }

    private void updateFqn() {
        connector.executeRead("MATCH (n:Package) WHERE (n)<-[:CONTAINS]-(:Test) RETURN n")
                .forEachRemaining((result) -> {
                    Node node = result.get("n").asNode();
                    String fqn = "test." + node.get("fqn").asString();

                    connector.executeWrite(
                            "MATCH (n) WHERE ID(n) = " + node.id() + " SET n.fqn = \'" + fqn + "\'"
                    );
                });
    }

    private void addTestSuiteRelation() {
        connector.executeWrite("MATCH (c:Class), (t:JUnit) WHERE c.fqn = t.name CREATE (t)-[:TESTS]->(c)");
    }

    private void addTestRelation() {
        connector.executeWrite("MATCH (ts:TestSuite)-[:CONTAINS]->(t:TestCase), (c:Class)-[:DECLARES]->(m:Method) " +
                "WHERE ts.name = c.fqn and t.name = m.name CREATE (t)-[:TESTS]->(m)");
    }
}
