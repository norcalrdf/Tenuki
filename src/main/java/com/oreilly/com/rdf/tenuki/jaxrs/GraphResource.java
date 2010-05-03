package com.oreilly.com.rdf.tenuki.jaxrs;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.restlet.data.MediaType;
import org.restlet.resource.StringRepresentation;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

@Path("/graphs/")
public class GraphResource {

	private Dataset dataset;

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Produces("text/uri-list")
	@GET
	public String getGraphsAsURIList() {
		StringBuilder urilist = new StringBuilder();
		for (Iterator<String> iterator = dataset.listNames(); iterator
				.hasNext();) {
			String name = iterator.next();
			urilist.append(name);
			urilist.append("\r\n");
		}
		return urilist.toString();
	}
	
	@Path("{graphUri}")
	@Produces("application/rdf+xml")
	@GET
	public Model getGraph(@PathParam("graphUri") String graphUri) {
		return dataset.getNamedModel(graphUri);
	}

	@Path("{graphUri}")
	@Consumes("application/rdf+xml")
	@PUT
	public void setGraph(@PathParam("graphUri") String graphUri, Model model) {
		Model dsModel = dataset.getNamedModel(graphUri);
		dsModel.add(model);
	}

}
