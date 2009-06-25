package com.oreilly.rdf.changes.restlet;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.oreilly.rdf.changes.Changeset;
import com.oreilly.rdf.changes.ChangesetHandler;
import com.oreilly.rdf.changes.InputStreamChangeset;

public class ChangesetResource extends JenaModelResource {

	private Log log = LogFactory.getLog(ChangesetResource.class);

	public ChangesetResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.valueOf("CHANGESET")));
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Model perGraphModel = null;
		try {
			Changeset changeset = new InputStreamChangeset(entity.getStream());
			perGraphModel = this.getModel(changeset.getSubjectOfChange().getURI());
			ChangesetHandler handler = new ChangesetHandler(perGraphModel);
			try {
				writeLock();
				handler.applyChangeset(changeset);
			} finally {
				releaseLocks();
			}
		} catch (IOException e) {
			throw new ResourceException(e);
		} 
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public boolean allowPut() {
		return false;
	}

}
