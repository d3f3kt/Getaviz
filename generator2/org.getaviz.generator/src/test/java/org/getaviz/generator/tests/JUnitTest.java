package org.getaviz.generator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.getaviz.generator.Generator;
import org.getaviz.generator.city.m2m.City2City;
import org.getaviz.generator.city.m2t.City2AFrame;
import org.getaviz.generator.city.s2m.JQA2City;
import org.getaviz.generator.database.DatabaseConnector;
import org.getaviz.generator.jqa.DatabaseBuilder;
import org.getaviz.generator.jqa.JQA2JSON;
import org.getaviz.generator.mockups.JUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.v1.Record;

public class JUnitTest {

	static DatabaseConnector connector;
	static JUnit mockup = new JUnit();

	@BeforeAll
	static void setup() {
		mockup.setupDatabase("./test/databases/JUnit.db");
		mockup.loadProperties("JUnitTest.properties");
		connector = mockup.getConnector();

		//Generator.run();
		//new DatabaseBuilder();
		new JQA2City();
		new JQA2JSON();
		new City2City();
		new City2AFrame();
	}
	
	@AfterAll
	static void close(){
	}

	@Test
	void numberOfVisualizedPackages() {
		assertEquals(3, 3);
	}
}
