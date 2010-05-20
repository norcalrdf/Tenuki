package com.oreilly.rdf.tenuki.api;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GraphResourceTest extends APITest {

	@Test
	public void testPut() throws Exception {
		ClassPathResource sampleGraphCPResource = new ClassPathResource(
				"graph.xml");
		WebResource webResource = resource();
		ClientResponse resp = webResource.path("graphs/test").type(
				"application/rdf+xml").put(ClientResponse.class,
				sampleGraphCPResource.getFile());
		Assert.assertEquals(204, resp.getStatus());
	}

	@Test
	public void testGetRepresentation() throws Exception {
		ClassPathResource parentCPR = new ClassPathResource("parent.xml");
		WebResource webResource = resource();
		ClientResponse resp = webResource.path("graphs/test2").type(
				"application/rdf+xml").put(ClientResponse.class,
				parentCPR.getFile());
		Assert.assertEquals(204, resp.getStatus());
		ClientResponse resp2 = resource().path("graphs/test2").get(
				ClientResponse.class);
		Assert.assertEquals(200, resp2.getStatus());
	}

	@Test
	public void testPatch() throws Exception {
		ClassPathResource changeMeCPR = new ClassPathResource("changeme.xml");
		ClassPathResource changesetCPR = new ClassPathResource("changeset.xml");
		WebResource webResource = resource();
		ClientResponse resp = webResource.path("graphs/changes").type(
				"application/rdf+xml").put(ClientResponse.class,
				changeMeCPR.getFile());
		Assert.assertEquals(204, resp.getStatus());
		ClientResponse resp2 = resource().path("graphs/changes").method(
				"PATCH", ClientResponse.class, changesetCPR.getFile());
		Assert.assertEquals(200, resp2.getStatus());
	}

}
