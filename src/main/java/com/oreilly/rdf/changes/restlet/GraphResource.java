package com.oreilly.rdf.changes.restlet;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;

public class GraphResource extends JenaModelResource {


	private String graphName;

	public GraphResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.APPLICATION_RDF_XML));
		graphName = (String) getRequest().getAttributes().get("graphName");
	}

	@Override
	public void removeRepresentations() throws ResourceException {
		
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
