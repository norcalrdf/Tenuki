package com.oreilly.rdf.changes;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

public class TestRDBModelWithChangeLoad {

	public static final String TESTING_RESOURCE_URI = "http://example.com/res#thing";
	public static final String BEFORE_TITLE = "Original Title";
	public static final String AFTER_TITLE = "New Title";

	// database URL
	public static final String M_DB_URL = "jdbc:postgresql://192.168.100.129/jenardb";
	// User name
	public static final String M_DB_USER = "jena";
	// Password
	public static final String M_DB_PASSWD = "jena";
	// Database engine name
	public static final String M_DB = "PostgreSQL";
	// JDBC driver
	public static final String M_DBDRIVER_CLASS = "org.postgresql.Driver";
	
	private Model model;
	private DBConnection connection;

	@Before
	public void setUp() throws Exception {
		Class.forName(M_DBDRIVER_CLASS);
		connection = new DBConnection(M_DB_URL, M_DB_USER, M_DB_PASSWD, M_DB);
		ModelMaker maker = ModelFactory.createModelRDBMaker(connection);
		model = maker.createFreshModel();
	}

	@After
	public void tearDown() throws Exception {
		model.close();
		connection.cleanDB();
	}

	@Test
	public void testApplyChangeset() throws Exception {
		ClassPathResource changesetResource = new ClassPathResource(
				"changeset.xml");
		Changeset changeset = new InputStreamChangeset(changesetResource
				.getInputStream());
		ChangesetHandler handler = new ChangesetHandler(model);
		handler.applyChangeset(changeset);
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		Statement result = testingResource.getProperty(DC.title);
		Literal title = result.getLiteral();
		Assert.assertEquals(AFTER_TITLE, title.getLexicalForm());
	}

}
