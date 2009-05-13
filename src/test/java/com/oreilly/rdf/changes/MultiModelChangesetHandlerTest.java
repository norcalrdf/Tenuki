package com.oreilly.rdf.changes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;

public class MultiModelChangesetHandlerTest {
	public static final String TESTING_RESOURCE_URI = "http://example.com/res#thing";
	public static final String BEFORE_TITLE = "Original Title";
	public static final String AFTER_TITLE = "New Title";


	private class MockChangeSet implements Changeset {

		@Override
		public Statement[] toAdd() {
			Model model = ModelFactory.createDefaultModel();
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			testingResource.addProperty(DC.title, AFTER_TITLE);
			return allProperties(testingResource);
		}

		@Override
		public Statement[] toRemove() {

			Model model = ModelFactory.createDefaultModel();
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			testingResource.addProperty(DC.title, BEFORE_TITLE);
			return allProperties(testingResource);
		}

		private Statement[] allProperties(Resource testingResource) {
			ArrayList<Statement> statements = new ArrayList<Statement>();
			StmtIterator iter = testingResource.listProperties();
			while (iter.hasNext()) {
				statements.add(iter.nextStatement());
			}
			return  statements.toArray(new Statement[1]);
		}

		@Override
		public Resource getSubjectOfChange() {
			Model model = ModelFactory.createDefaultModel();
			return model.createResource("http://example.com/res#thing");
		}

	}

	private List<Model> models;
	private IDBConnection conn;

	@Before
	public void setUp() throws Exception {
		String className = "org.hsqldb.jdbcDriver";       // path of driver class
		Class.forName (className);                        // Load the Driver
		String DB_URL =    "jdbc:hsqldb:mem:testing";   // URL of database 
		String DB_USER =   "sa";                          // database user id
		String DB_PASSWD = "";                            // database password
		String DB =        "HSQL";                        // database type
		// Create database connection
		conn = new DBConnection ( DB_URL, DB_USER, DB_PASSWD, DB );
		ModelMaker maker = ModelFactory.createModelRDBMaker(conn) ;
		models = new ArrayList<Model>(2);
		models.add(maker.createFreshModel());
		models.add(maker.createFreshModel());

	}
	
	public void teardown() throws Exception {
		for (Model model : models) {
			model.close();
		}
		conn.cleanDB();
		conn.close();
	}

	@Test
	public void testApplyChangeset() {
		assertEquals(2, models.size());
		for (Model model : models) {
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			testingResource.addProperty(DC.title, BEFORE_TITLE);
			model.commit();
		}
		MultiModelChangesetHandler handler = new MultiModelChangesetHandler(models);
		Changeset changeset = new MockChangeSet();
		handler.applyChangeset(changeset);
		for (Model model : models) {
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			Statement result = testingResource.getProperty(DC.title);
			Literal title = result.getLiteral();
			Assert.assertEquals(AFTER_TITLE, title.getLexicalForm());
		}
	}

}
