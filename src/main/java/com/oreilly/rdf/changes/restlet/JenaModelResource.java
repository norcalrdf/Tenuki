package com.oreilly.rdf.changes.restlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.pool.ObjectPool;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.oreilly.rdf.jena.ModelPoolableFactory;

public abstract class JenaModelResource extends Resource {


	public JenaModelResource(Context content, Request request, Response responce) {
		super(content, request, responce);
	}

	private Dataset getDataset() {
		RDFModelApplication app = (RDFModelApplication) getApplication();
		String location = app.getTDBLocation();
		Dataset set = TDBFactory.createDataset(location);
		return set;
	}
	
	public Model getDefaultModel() {
		try {
			Dataset set = getDataset();
			return set.getDefaultModel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Model getModel(String modelName) {
		try {
			return getDataset().getNamedModel(modelName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void deleteModel(String modelName) {
		Model model = getModel(modelName);		
	}
	
	public void deleteAll() {
		for (String modelName : modelNames()) {
			getModel(modelName).removeAll();
			TDB.sync(getDataset());
		}
	}
	
	public List<String> modelNames() {
		try {
			List<String> list = new ArrayList<String>();
 			for (Iterator<String> iterator = getDataset().listNames(); iterator.hasNext();) {
				list.add(iterator.next());
			}
 			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
  }
