package com.oreilly.rdf.changes;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;
import com.oreilly.rdf.changes.jms.ChangeMessageProcessor;

public class TestJMSChanges {
	public static final String TESTING_RESOURCE_URI = "http://example.com/res#thing";
	public static final String BEFORE_TITLE = "Original Title";
	public static final String AFTER_TITLE = "New Title";

	public static final String TESTING_TOPIC = "test.rdf.changes";
	// database URL
	public static final String M_DB_URL = "jdbc:postgresql://thunderbrick.west.ora.com/jenardb";
	// User name
	public static final String M_DB_USER = "jena";
	// Password
	public static final String M_DB_PASSWD = "jena";
	// Database engine name
	public static final String M_DB = "PostgreSQL";
	// JDBC driver
	public static final String M_DBDRIVER_CLASS = "org.postgresql.Driver";
	
	private Model model;
	private DBConnection connection;

	@Before
	public void setUp() throws Exception {
		Class.forName(M_DBDRIVER_CLASS);
		connection = new DBConnection(M_DB_URL, M_DB_USER, M_DB_PASSWD, M_DB);
		ModelMaker maker = ModelFactory.createModelRDBMaker(connection);
		model = maker.createFreshModel();
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		testingResource.addProperty(DC.title, BEFORE_TITLE);
		model.commit();
	}

	@After
	public void tearDown() throws Exception {
		model.close();
		connection.cleanDB();
	}

	private static class MockChangeMessageProducer implements Runnable {
		public void run() {
			try {
			    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
				Connection connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createTopic(TESTING_TOPIC);
				MessageProducer producer = session.createProducer(destination);
				ClassPathResource changesetResource = new ClassPathResource(
				"changeset.xml");
				String changesetXml = IOUtils.toString(changesetResource.getInputStream());
				TextMessage changeMessage = session.createTextMessage(changesetXml);
				producer.send(changeMessage);
				session.close();
				connection.close();
			} catch (Exception e) {
				Assert.fail();
			}

		}
	}

	@Test
	public void testChangeMessageApplied() throws Exception {
	    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		Thread consumer = new Thread(new ChangeMessageProcessor(model, TESTING_TOPIC, connectionFactory));
		Thread producer = new Thread(new MockChangeMessageProducer());
		consumer.start();
		producer.start();
		producer.join();
		Thread.sleep(1000);
		Resource testingResource = model.createResource(TESTING_RESOURCE_URI);
		Statement result = testingResource.getProperty(DC.title);
		Literal title = result.getLiteral();
		Assert.assertEquals(AFTER_TITLE, title.getLexicalForm());
	}

}
