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

    private void addTestRelation() {
        connector.executeWrite("MATCH (c:Class), (t:JUnit) WHERE c.fqn = t.name CREATE (t)-[:TESTS]->(c)");
    }
}
