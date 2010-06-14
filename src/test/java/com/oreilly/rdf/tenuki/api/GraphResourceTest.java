package com.oreilly.rdf.tenuki.api;

import java.net.URI;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.oreilly.http.HttpPatchSender;
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
		ClientResponse resp =
            webResource.path("graphs/changes").type("application/rdf+xml").put(ClientResponse.class,
                                                                               changeMeCPR.getFile());
        Assert.assertEquals(204, resp.getStatus());

		WebResource res = webResource.path("graphs/changes");
		URI uri = res.getURI();
		HttpPatchSender hps = new HttpPatchSender();
		hps.setUri(uri);
		hps.setMimeType("application/vnd.talis.changeset+xml");
		hps.setFile(changesetCPR.getFile());
		HttpResponse resp2 = hps.send();
		StatusLine sl = resp2.getStatusLine();
		System.out.println(sl.getReasonPhrase());
		Assert.assertEquals(200, sl.getStatusCode());
	}

}
