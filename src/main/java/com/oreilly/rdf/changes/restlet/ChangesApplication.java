package com.oreilly.rdf.changes.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.MediaType;

import com.hp.hpl.jena.rdf.model.Model;

public class ChangesApplication extends Application {

	private Model model;

	@Override
	public Restlet createRoot() {
		MediaType.register("CHANGESET", "application/vnd.talis.changeset+xml");
		Router router = new Router(getContext());
		router.attach("/changes", ChangesetResource.class);
		return router;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
}
