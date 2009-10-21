package com.oreilly.rdf.tenuki.restlet;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.util.Series;
import org.springframework.core.io.ClassPathResource;

public class GraphResourceTest {

	private static final String HOST = "http://localhost:8182";
	private Client client;
	private Server server;
	private ClassPathResource sampleGraphCPResource;

	@Before
	public void setUp() throws Exception {
		sampleGraphCPResource = new ClassPathResource("graph.xml");
		client = new Client(Protocol.HTTP);
		Restlet restlet = new RDFModelApplication();
		Context context = new Context();
		context.getParameters().add("tdb.location", "testing_tdb");
		server = new Server(Protocol.HTTP, 8182, restlet);
		server.setContext(context);
		server.start();
	}
	
	@After
	public void tearDown() throws Exception {
		server.stop();
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
