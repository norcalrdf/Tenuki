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

import com.hp.hpl.jena.rdf.model.Model;
import com.oreilly.rdf.tenuki.Changeset;
import com.oreilly.rdf.tenuki.ChangesetHandler;

@Path("/graphs/")
public class GraphResource extends DatasetAccessResource {

	@Produces("text/uri-list")
	@GET
	public String getGraphsAsURIList() {
		StringBuilder urilist = new StringBuilder();
		for (Iterator<String> iterator = getDataset().listNames(); iterator
				.hasNext();) {
			String name = iterator.next();
			urilist.append(name);
			urilist.append("\r\n");
		}
		return urilist.toString();
	}
	
	@Produces({"application/rdf+xml", "text/turtle", "text/rdf+n3", "text/plain"})
	@GET
	public Model getGraphByQueryParam(@QueryParam("graph") String graphUri) {
		return getDataset().getNamedModel(graphUri);
	}

	@Path("{graphUri}")
	@Produces({"application/rdf+xml", "text/turtle", "text/rdf+n3", "text/plain"})
	@GET
	public Model getGraph(@PathParam("graphUri") String graphUri) {
		return getDataset().getNamedModel(graphUri);
	}

	@Path("{graphUri}")
	@Consumes({"application/rdf+xml", "text/turtle", "text/rdf+n3", "text/plain"})
	@PUT
	public Response updateGraph(@PathParam("graphUri") String graphUri, Model model) {
		Model dsModel = getDataset().getNamedModel(graphUri);
		try {
			if (dsModel.supportsTransactions()) {
				dsModel.begin();
			}
			dsModel.add(model);
			dsModel.close();
			if (dsModel.supportsTransactions()) {
				dsModel.commit();
			}
		} catch (RuntimeException e) {
			if (dsModel.supportsTransactions()) {
				dsModel.abort();
			}
		}
		return Response.noContent().build();
	}
	
	@Path("{graphUri}")
	@Consumes({"application/rdf+xml", "text/turtle", "text/rdf+n3", "text/plain"})
	@POST
	public Response setGraph(@PathParam("graphUri") String graphUri, Model model) {
		Model dsModel = getDataset().getNamedModel(graphUri);
		try {
			if (dsModel.supportsTransactions()) {
				dsModel.begin();
			}
			dsModel.removeAll();
			dsModel.add(model);
			dsModel.close();
			if (dsModel.supportsTransactions()) {
				dsModel.commit();
			}
		} catch (RuntimeException e) {
			if (dsModel.supportsTransactions()) {
				dsModel.abort();
			}
		}
		return Response.noContent().build();
	}

	
	@Path("{graphUri}")
	@DELETE
	public Response deleteGraph(@PathParam("graphUri") String graphUri) {
		Model dsModel = getDataset().getNamedModel(graphUri);
		try {
			if (dsModel.supportsTransactions()) {
				dsModel.begin();
			}
			dsModel.removeAll();
			dsModel.close();
			if (dsModel.supportsTransactions()) {
				dsModel.commit();
			}
		} catch (RuntimeException e) {
			if (dsModel.supportsTransactions()) {
				dsModel.abort();
			}
		}
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
		if ("changes".equals(graphUri) || subject.equals(graphUri)) {
			Model dsModel = getDataset().getNamedModel(graphUri);
			ChangesetHandler handler = new ChangesetHandler(dsModel);
			handler.applyChangeset(changeset);
			return Response.ok(dsModel,
					MediaType.valueOf("application/rdf+xml")).build();
		} else {
			return Response.serverError().build();
		}

	}


}
