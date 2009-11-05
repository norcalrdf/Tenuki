package com.oreilly.rdf.tenuki.restlet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.data.Tag;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;


public class GraphResourceTest extends RestletTest {

	@Test
	public void testStoreRepresentation() throws Exception {
		Representation entity = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		Response resp = client.put(HOST + "/graphs/test", entity);
		assertEquals(Status.SUCCESS_OK, resp.getStatus());
	}
	
	@Test
	public void testStoreRepresentationWithInvalidEtag() throws Exception {
		Representation entity = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		Request request = new Request(Method.PUT, HOST + "/graphs/test", entity);
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag("DON'T MATCH"));
		request.getConditions().setMatch(tags);
		Response resp = client.handle(request);
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED, resp.getStatus());
	}
	
	@Test
	public void testStoreRepresentationWithValidEtag() throws Exception {
		Representation entity1 = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		client.put(HOST + "/graphs/test", entity1);
		Response g_resp = client.get(HOST + "/graphs/test");
		Representation entity2 = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		Request request = new Request(Method.PUT, HOST + "/graphs/test", entity2);
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(g_resp.getEntity().getTag());
		request.getConditions().setMatch(tags);
		Response resp = client.handle(request);
		assertEquals(Status.SUCCESS_OK, resp.getStatus());
	}

	
	@Test
	public void testAcceptRepresentation() throws Exception {
		Representation entity = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		Response resp = client.post(HOST + "/graphs/test", entity);
		assertEquals(Status.SUCCESS_CREATED, resp.getStatus());
	}

	@Test
	public void testGetRepresentation() throws Exception {
		Representation entity = new InputRepresentation(sampleGraphCPResource
				.getInputStream(), MediaType.APPLICATION_RDF_XML);
		client.post(HOST + "/graphs/test", entity);
		Response resp = client.get(HOST + "/graphs/test");
		assertEquals(Status.SUCCESS_OK, resp.getStatus());
	}

}
