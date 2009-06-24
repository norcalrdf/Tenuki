package com.oreilly.rdf.changes.restlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDB;

public class GraphResource extends JenaModelResource {

	private String graphName;

	public GraphResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.APPLICATION_RDF_XML));
		graphName = (String) getRequest().getAttributes().get("graphName");
		try {
			graphName = URLDecoder.decode(graphName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		if (MediaType.APPLICATION_RDF_XML.equals(entity.getMediaType())) {
			Model newModel = ModelFactory.createDefaultModel();
			try {
				newModel.read(entity.getStream(), "");
			} catch (IOException e) {
				throw new ResourceException(e);
			}
			Model model = getModel(graphName);
			model.removeAll();
			model.add(newModel);
			TDB.sync(model);
		}
		getResponse().setStatus(Status.SUCCESS_CREATED);
	}

	@Override
	public boolean allowDelete() {
		return true;
	}

	@Override
	public void removeRepresentations() throws ResourceException {
		Model model = getModel(graphName);
		model.removeAll();
		model.commit();
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		if (MediaType.APPLICATION_RDF_XML.equals(variant.getMediaType())) {
			Model model = getModel(graphName);
			return new WholeModelRepresentation(model);
		}
		return null;
	}
}
