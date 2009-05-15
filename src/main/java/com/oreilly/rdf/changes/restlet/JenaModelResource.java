package com.oreilly.rdf.changes.restlet;

import java.util.List;

import org.apache.commons.pool.ObjectPool;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.hp.hpl.jena.rdf.model.Model;
import com.oreilly.rdf.jena.ModelPoolableFactory;

public abstract class JenaModelResource extends Resource {

	private ObjectPool pool;
	private ModelPoolableFactory factory;

	public JenaModelResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		RDFModelApplication app = (RDFModelApplication) getApplication();
		pool = app.getModelPool();
		factory = app.getFactory();
	}

	public Model getDefaultModel() {
		try {
			return (Model) pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void returnDefaultModel(Model model) {
		try {
			pool.returnObject(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Model getModel(String modelName) {
		try {
			return factory.getModel(modelName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void returnModel(Model model) {
		try {
			factory.destroyObject(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<String> modelNames() {
		try {
			return factory.listModels();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
  }
