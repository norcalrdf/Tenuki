package com.oreilly.rdf.changes.restlet;

import org.apache.commons.pool.ObjectPool;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.MediaType;
import org.springframework.beans.factory.annotation.Required;

import com.oreilly.rdf.jena.ModelPoolableFactory;

public class RDFModelApplication extends Application {

	private ObjectPool modelPool;
	private ModelPoolableFactory factory;

	@Override
	public Restlet createRoot() {
		MediaType.register("CHANGESET", "application/vnd.talis.changeset+xml");
		Router router = new Router(getContext());
		router.attach("/changes", ChangesetResource.class);
		router.attach("/graphs/", GraphsResource.class);
		router.attach("/graphs/{graphName}", GraphResource.class);
		return router;
	}
	
	public ObjectPool getModelPool(){
		return this.modelPool;
	}
	
	public void setModelPool(ObjectPool pool) {
		this.modelPool = pool;
	}

	@Required
	public void setFactory(ModelPoolableFactory factory) {
		this.factory = factory;
	}

	public ModelPoolableFactory getFactory() {
		return factory;
	}
}
