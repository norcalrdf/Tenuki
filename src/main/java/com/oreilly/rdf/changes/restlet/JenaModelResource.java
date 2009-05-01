package com.oreilly.rdf.changes.restlet;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.pool.ObjectPool;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.DoesNotExistException;

public abstract class JenaModelResource extends Resource {

	private ObjectPool pool;

	public JenaModelResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		RDFModelApplication app = (RDFModelApplication) getApplication();
		pool = app.getModelPool();
	}

	public Model getModel() {
		try {
			return (Model) pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void returnModel(Model model) {
		try {
			pool.returnObject(model);
		} catch (Exception e) {
			//ignore
		}
	}
}
