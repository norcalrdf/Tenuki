package com.oreilly.rdf.changes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.DC;



public class TestChangeLoader {
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
		testingResource.addProperty(DC.title, BEFORE_TITLE);
		model.commit();

	}

	@After
	public void tearDown() throws Exception {
		this.model.close();
		FileUtils.deleteDirectory(testDirectory);
	}

	@Test
	public void testApplyChangeset() {
		ChangesetHandler handler = new ChangesetHandler(model);
		Changeset changeset = new MockChangeSet();
		handler.applyChangeset(changeset);
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		Statement result = testingResource.getProperty(DC.title);
		Literal title = result.getLiteral();
		Assert.assertEquals(AFTER_TITLE, title.getLexicalForm());
	}
	
	@Test
	public void testApplyChangesetFile() throws Exception {
		ClassPathResource changesetResource = new ClassPathResource("changeset.xml");
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
