package org.getaviz.generator.mockups;

import java.io.File;

import io.github.cdimascio.dotenv.Dotenv;
import org.getaviz.generator.database.DatabaseConnector;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class JUnit extends Mockup {

	public void setupDatabase(String directory) {
		connector = DatabaseConnector.getInstance("bolt://localhost:7687");
	}
}
