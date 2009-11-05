package com.oreilly.rdf.tenuki.restlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RelatedGraphsTest extends RestletTest {

	@Test
	public void testGetRelated() throws Exception {
		ClassPathResource parent = new ClassPathResource("parent.xml");
		ClassPathResource child = new ClassPathResource("child.xml");
		ClassPathResource grandchild = new ClassPathResource("grandchild.xml");
		Representation entity = new InputRepresentation(
				parent.getInputStream(), MediaType.APPLICATION_RDF_XML);
		client.post(HOST + "/graphs/http://example.com/1", entity);

		Representation childEntity = new InputRepresentation(child
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		client.post(HOST + "/graphs/http://example.com/2", childEntity);

		Representation grandchildEntity = new InputRepresentation(grandchild
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		client.post(HOST + "/graphs/http://example.com/3", grandchildEntity);

		Response resp = client.get(HOST + "/related/http://example.com/1");
		assertEquals(Status.SUCCESS_OK, resp.getStatus());
		Representation result = resp.getEntity();
		Model model = ModelFactory.createDefaultModel();
		model.read(result.getStream(), "");
		assertEquals(true, model.contains(model
				.getResource("http://example.com/3"), model
				.getProperty("http://example.com/ns/value"), "Test"));
	}
}
