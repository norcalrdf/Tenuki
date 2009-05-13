package com.oreilly.rdf.changes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.rdf.model.Resource;

public class InputStreamChangesetTest {

	private Changeset 		changeset;

	@Before
	public void setUp() throws Exception {
		ClassPathResource changesetResource = new ClassPathResource(
		"changeset.xml");
		changeset = new InputStreamChangeset(changesetResource
		.getInputStream());

	}
	
	@Test
	public void testGetSubjectOfChange() {
		Resource subject = changeset.getSubjectOfChange();
		assertEquals("http://example.com/res#thing", subject.getURI());
	}

}
