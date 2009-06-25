package com.oreilly.rdf.changes.restlet;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.data.MediaType;
import org.restlet.resource.OutputRepresentation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.tdb.store.GraphTDB;

public class WholeModelRepresentation extends OutputRepresentation {
	private Log log = LogFactory.getLog(WholeModelRepresentation.class);
	private Model model;
	private Lock lock;

	public WholeModelRepresentation(Model model, Lock lock) {
		super(MediaType.APPLICATION_RDF_XML);
		this.model = model;
		this.lock = lock;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		lock.enterCriticalSection(Lock.READ);
		try {
			model.write(output);
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		} finally {
			lock.leaveCriticalSection();
		}

	}

}
