package com.oreilly.rdf.changes.restlet;

import java.io.IOException;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.oreilly.rdf.changes.Changeset;
import com.oreilly.rdf.changes.ChangesetHandler;
import com.oreilly.rdf.changes.InputStreamChangeset;

public class ChangesetResource extends Resource {

	private ChangesetHandler handler;

	public ChangesetResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.valueOf("CHANGESET")));
		handler = new ChangesetHandler(((ChangesApplication) getApplication())
				.getModel());
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		try {
			Changeset changeset = new InputStreamChangeset(entity.getStream());
			handler.applyChangeset(changeset);
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
