package com.oreilly.rdf.tenuki.restlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.core.DatasetImpl;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.talis.tdb.bdb.BDBinstance;
import com.talis.tdb.bdb.SetupBDB;

public abstract class RestletTest {

	protected static final String HOST = "http://localhost:8182";
	protected Client client;
	private static Server server;
	protected ClassPathResource sampleGraphCPResource;

	@BeforeClass
	public static void before() throws Exception {
		Restlet restlet = new RDFModelApplication();
		Context context = new Context();
		context.getParameters().add("tdb-bdb.location", "testing_tdb-bdb");
		server = new Server(Protocol.HTTP, 8182, restlet);
		server.setContext(context);
		server.start();
	}

	@AfterClass
	public static void after() throws Exception {
		server.stop();
	}

	@Before
	public void setUp() throws Exception {
		sampleGraphCPResource = new ClassPathResource("graph.xml");
		client = new Client(Protocol.HTTP);
	}

	@After
	public void tearDown() {
		BDBinstance bdb = new BDBinstance("testing_tdb-bdb");
		DatasetGraphTDB dsg = SetupBDB.buildDataset(bdb);
		Dataset tdb = new DatasetImpl(dsg);
		List<String> list = new ArrayList<String>();
		for (Iterator<String> iterator = tdb.listNames(); iterator.hasNext();) {
			list.add(iterator.next());
		}
		for (String modelName : list) {
			tdb.getNamedModel(modelName).removeAll();
		}
	}

	public RestletTest() {
		super();
	}

}