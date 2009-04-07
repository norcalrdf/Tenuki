package com.oreilly.rdf.changes;

import java.io.File;
import java.io.IOException;

import org.junit.*;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.DC;



public class TestChangeLoader {
	public static final String TESTING_RESOURCE_URI = "urn:x-testing";

	private class MockChangeSet implements Changeset {


		@Override
		public StmtIterator toAdd() {
			Model model = ModelFactory.createDefaultModel();
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			testingResource.addProperty(DC.title, "after the change");
			return testingResource.listProperties();
		}

		@Override
		public StmtIterator toRemove() {
			Model model = ModelFactory.createDefaultModel();
			Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
			testingResource.addProperty(DC.title, "test");
			return testingResource.listProperties();
		}

	}

	public static File createTempDirectory() throws IOException {
		final File temp;
		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}
		return (temp);
	}

	private File testDirectory;
	private Model model;

	@Before
	public void setUp() throws Exception {
		testDirectory = createTempDirectory();
		model = TDBFactory.createModel(this.testDirectory
				.getAbsolutePath());
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		testingResource.addProperty(DC.title, "test");
		model.commit();

	}

	@After
	public void tearDown() throws Exception {
		this.model.close();
		this.testDirectory.delete();
	}

	@Test
	public void testApplyChangeset() {
		ChangesetHandler handler = new ChangesetHandler(model);
		Changeset changeset = new MockChangeSet();
		handler.applyChangeset(changeset);
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		Statement result = testingResource.getProperty(DC.title);
		Literal title = result.getLiteral();
		Assert.assertEquals("after the change", title.getLexicalForm());
	}
}
