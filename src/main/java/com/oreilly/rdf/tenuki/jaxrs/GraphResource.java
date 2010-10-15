package com.oreilly.rdf.tenuki.jaxrs;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.oreilly.rdf.tenuki.Changeset;
import com.oreilly.rdf.tenuki.ChangesetHandler;

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
	
	@Produces("application/rdf+xml")
	@GET
	public Model getGraphByQueryParam(@QueryParam("graph") String graphUri) {
		return dataset.getNamedModel(graphUri);
	}

	@Path("{graphUri}")
	@Produces("application/rdf+xml, text/turtle, text/rdf+n3, text/plain")
	@GET
	public Model getGraph(@PathParam("graphUri") String graphUri) {
		return dataset.getNamedModel(graphUri);
	}

	@Path("{graphUri}")
	@Consumes("application/rdf+xml, text/turtle, text/rdf+n3, text/plain")
	@PUT
	public Response setGraph(@PathParam("graphUri") String graphUri, Model model) {
		Model dsModel = dataset.getNamedModel(graphUri);
		dsModel.add(model);
		return Response.noContent().build();
	}
	
	@Path("{graphUri}")
	@DELETE
	public Response deleteGraph(@PathParam("graphUri") String graphUri) {
		Model dsModel = dataset.getNamedModel(graphUri);
		dsModel.removeAll();
		return Response.noContent().build();
	}

	@Path("{graphUri}")
	@Consumes("application/vnd.talis.changeset+xml")
	@PATCH
	public Response applyChangesetToGraph(
			@PathParam("graphUri") String graphUri, Changeset changeset) {
		return applyChangeset(graphUri, changeset);
	}
	
	@Path("{graphUri}/patch")
	@Consumes("application/vnd.talis.changeset+xml")
	@POST
	public Response applyChangesetToGraphPost(
			@PathParam("graphUri") String graphUri, Changeset changeset) {
		return applyChangeset(graphUri, changeset);
	}
	
	private Response applyChangeset(String graphUri, Changeset changeset) {
	    String subject = changeset.getSubjectOfChange().toString();
		if ("changes".equals(graphUri) || changeset.getSubjectOfChange().toString().equals(graphUri)) {
			Model dsModel = dataset.getNamedModel(graphUri);
			ChangesetHandler handler = new ChangesetHandler(dsModel);
			handler.applyChangeset(changeset);
			return Response.ok(dsModel,
					MediaType.valueOf("application/rdf+xml")).build();
		} else {
			return Response.serverError().build();
		}

	}


}
