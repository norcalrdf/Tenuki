package com.oreilly.rdf.changes.restlet;

import java.io.IOException;
import java.io.OutputStream;

import org.restlet.data.MediaType;
import org.restlet.resource.OutputRepresentation;

import com.hp.hpl.jena.rdf.model.Model;

public class WholeModelRepresentation extends OutputRepresentation {

	private Model model;

	public WholeModelRepresentation(Model model) {
		super(MediaType.APPLICATION_RDF_XML);
		this.model = model;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		model.write(output);
	}

}
