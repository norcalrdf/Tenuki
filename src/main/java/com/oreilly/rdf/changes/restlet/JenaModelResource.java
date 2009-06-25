package com.oreilly.rdf.changes.restlet;

import java.util.Iterator;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.tdb.TDBFactory;

public abstract class JenaModelResource extends Resource {


	private static final String TDB_LOCATION_PARAM = "tdb.location";

	public JenaModelResource(Context content, Request request, Response responce) {
		super(content, request, responce);
	}
	
	protected void readLock() {
		getDataset().getLock().enterCriticalSection(Lock.READ);
	}
	
	protected void writeLock() {
		getDataset().getLock().enterCriticalSection(Lock.WRITE);
	}
	
	protected void releaseLocks() {
		getDataset().getLock().leaveCriticalSection();
	}
	
	protected Lock getDatasetLock() {
		return getDataset().getLock();
	}

	private Dataset getDataset() {
		String location = getContext().getParameters().getFirstValue(TDB_LOCATION_PARAM);
		Dataset set = TDBFactory.createDataset(location);
		return set;
	}
	
	public Model getDefaultModel() {
		Dataset set = getDataset();
		return set.getDefaultModel();
	}
	
	public Model getModel(String modelName) {
		return getDataset().getNamedModel(modelName);
	}
	
	public Iterator<String> modelNames() {
		return getDataset().listNames();
	}
  }
