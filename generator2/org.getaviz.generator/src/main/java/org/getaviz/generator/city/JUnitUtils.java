package org.getaviz.generator.city;

import org.getaviz.generator.SettingsConfiguration;
import org.getaviz.generator.city.m2m.RGBColor;
import org.getaviz.generator.database.DatabaseConnector;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.driver.v1.types.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JUnitUtils {

    private static SettingsConfiguration config = SettingsConfiguration.getInstance();
    private static DatabaseConnector connector = DatabaseConnector.getInstance();

    private List<Long> buildingCache;
    private List<Long> buildingSegmentCache;

    public boolean isTestBuilding(Node building) {
        if (buildingCache == null) {
            initializeBuildingCache();
        }

        return buildingCache.contains(building.id());
    }

    public Node getBuildingTestNode(Node building) {
        try {
            Record r = connector.executeRead(
                    "MATCH(b:Building)-[:VISUALIZES]->(c:Class)<-[:TESTS]-(t) WHERE ID(b) = " + building.id() + " RETURN t"
            ).single();

            return r.get("t").asNode();
        } catch (NoSuchRecordException e) {
            return null;
        }
    }

    public boolean isTestBuildingSegment(Node buildingSegment) {
        return isTestBuildingSegment(buildingSegment.id());
    }

    public boolean isTestBuildingSegment(Long buildingSegmentId) {
        if (buildingSegmentCache == null) {
            initializeBuildingSegmentCache();
        }

        return buildingSegmentCache.contains(buildingSegmentId);
    }

    public Node getBuildingSegmentTestNode(Node buildingSegmentNode) {
        return getBuildingSegmentTestNode(buildingSegmentNode.id());
    }

    public Node getBuildingSegmentTestNode(Long segmentId) {
        try {
            Record r = connector.executeRead(
                    "MATCH (bs:BuildingSegment)-[:VISUALIZES]->(m:Method)<-[:TESTS]-(t:TestCase) " +
                            "WHERE ID(bs) = " + segmentId + " RETURN t"
            ).single();

            return r.get("t").asNode();
        } catch (NoSuchRecordException e) {
            return null;
        }
    }

    public String getSegmentColor(Node buildingSegmentNode) {
        return getSegmentColor(buildingSegmentNode.id());
    }

    public String getSegmentColor(Long buildingSegmentId) {
        Node testNode = getBuildingSegmentTestNode(buildingSegmentId);

        String color = config.getTestClassColorNeutralHex();

        if(testNode != null) {
            if (testNode.get("result").asString().equals("SUCCESS")) {
                color = config.getTestClassColorSuccessHex();
            } else {
                color = config.getTestClassColorFailedHex();
            }
        }

        if (config.getOutputFormat() == SettingsConfiguration.OutputFormat.AFrame) {
            return color;
        }else {
            return new RGBColor(Color.decode(color)).asPercentage();
        }

    }

    private void initializeBuildingCache() {
        buildingCache = new ArrayList<Long>();

        connector.executeRead("MATCH(b:Building)-[:VISUALIZES]->(c:Class)<-[:CONTAINS]-(p:Package) " +
                "WHERE p.fqn =~ 'test.*'  return b").forEachRemaining((result) -> {
            buildingCache.add(result.get("b").asNode().id());
        });
    }

    private void initializeBuildingSegmentCache() {
        buildingSegmentCache = new ArrayList<Long>();

        connector.executeRead("MATCH (bs:BuildingSegment)<-[:CONTAINS]-(b:Building)-[:VISUALIZES]->(c:Class)<-[:CONTAINS]-(p:Package) " +
                "WHERE p.fqn =~ 'test.*' return bs").forEachRemaining((result) -> {
            buildingSegmentCache.add(result.get("bs").asNode().id());
        });
    }
}
