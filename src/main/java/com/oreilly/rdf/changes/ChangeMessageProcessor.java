package com.oreilly.rdf.changes;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.io.IOUtils;

import com.hp.hpl.jena.rdf.model.Model;

public class ChangeMessageProcessor implements Runnable {

	private Model model;
	private ConnectionFactory connectionFactory;
	private String topicName;
	private AtomicInteger numberProcessed;

	public ChangeMessageProcessor(Model model, String topic,
			ConnectionFactory connectionFactory) {
		this.model = model;
		this.connectionFactory = connectionFactory;
		this.topicName = topic;
		this.setNumberProcessed(new AtomicInteger(0));
	}

	@Override
	public void run() {
		try {
			ChangesetHandler handler = new ChangesetHandler(model);
			MessageConsumer consumer = createConsumer();
			receiveLoop(handler, consumer);
		} catch (JMSException e) {
			//TODO: Do something!
		}
	}

	private void receiveLoop(ChangesetHandler handler, MessageConsumer consumer)
			throws JMSException {
		while (true){
			Message message = consumer.receive();
			if (message instanceof TextMessage) {
				handleMessage(handler, (TextMessage) message);
			}
		}
	}

	private void handleMessage(ChangesetHandler handler, TextMessage message)
			throws JMSException {
		String xml = message.getText();
		Changeset changeset = new InputStreamChangeset(IOUtils.toInputStream(xml));
		handler.applyChangeset(changeset);
		getNumberProcessed().getAndIncrement();
	}

	private MessageConsumer createConsumer() throws JMSException {
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createTopic(topicName);
		MessageConsumer consumer = session.createConsumer(destination);
		return consumer;
	}

	private void setNumberProcessed(AtomicInteger numberProcessed) {
		this.numberProcessed = numberProcessed;
	}

	public AtomicInteger getNumberProcessed() {
		return numberProcessed;
	}

}
