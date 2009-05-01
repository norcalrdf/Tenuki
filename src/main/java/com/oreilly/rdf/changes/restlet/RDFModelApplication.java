package com.oreilly.rdf.changes.restlet;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.pool.ObjectPool;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.MediaType;

public class RDFModelApplication extends Application {

	private ObjectPool modelPool;

	@Override
	public Restlet createRoot() {
		MediaType.register("CHANGESET", "application/vnd.talis.changeset+xml");
		Router router = new Router(getContext());
		router.attach("/changes", ChangesetResource.class);
		return router;
	}
	
	public ObjectPool getModelPool(){
		return this.modelPool;
	}
	
	public void setModelPool(ObjectPool pool) {
		this.modelPool = pool;
	}
}
