package com.oreilly.rdf.tenuki.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GraphResourceTest extends APITest {

	@Test
	public void testStoreRepresentation() throws Exception {
		ClassPathResource sampleGraphCPResource = new ClassPathResource(
				"graph.xml");
		WebResource webResource = resource();
		ClientResponse resp = webResource.path("graphs/test").type("application/rdf+xml").put(
				ClientResponse.class, sampleGraphCPResource.getFile());
		assertEquals(204, resp.getStatus());
	}

	// public void testStoreRepresentationWithInvalidEtag() throws Exception {
	// Representation entity = new InputRepresentation(sampleGraphCPResource
	// .getInputStream(), MediaType.APPLICATION_RDF_XML);
	// Request request = new Request(Method.PUT, HOST + "/graphs/test", entity);
	// List<Tag> tags = new ArrayList<Tag>();
	// tags.add(new Tag("DON'T MATCH"));
	// request.getConditions().setMatch(tags);
	// Response resp = client.handle(request);
	// assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED, resp.getStatus());
	// }
	//	
	// public void testStoreRepresentationWithValidEtag() throws Exception {
	// Representation entity1 = new InputRepresentation(sampleGraphCPResource
	// .getInputStream(), MediaType.APPLICATION_RDF_XML);
	// client.put(HOST + "/graphs/test", entity1);
	// Response g_resp = client.get(HOST + "/graphs/test");
	// Representation entity2 = new InputRepresentation(sampleGraphCPResource
	// .getInputStream(), MediaType.APPLICATION_RDF_XML);
	// Request request = new Request(Method.PUT, HOST + "/graphs/test",
	// entity2);
	// List<Tag> tags = new ArrayList<Tag>();
	// tags.add(g_resp.getEntity().getTag());
	// request.getConditions().setMatch(tags);
	// Response resp = client.handle(request);
	// assertEquals(Status.SUCCESS_OK, resp.getStatus());
	// }
	//
	//	
	// public void testAcceptRepresentation() throws Exception {
	// Representation entity = new InputRepresentation(sampleGraphCPResource
	// .getInputStream(), MediaType.APPLICATION_RDF_XML);
	// Response resp = client.post(HOST + "/graphs/test", entity);
	// assertEquals(Status.SUCCESS_CREATED, resp.getStatus());
	// }
	//
	// public void testGetRepresentation() throws Exception {
	// Representation entity = new InputRepresentation(sampleGraphCPResource
	// .getInputStream(), MediaType.APPLICATION_RDF_XML);
	// client.post(HOST + "/graphs/test", entity);
	// Response resp = client.get(HOST + "/graphs/test");
	// assertEquals(Status.SUCCESS_OK, resp.getStatus());
	// }

}
