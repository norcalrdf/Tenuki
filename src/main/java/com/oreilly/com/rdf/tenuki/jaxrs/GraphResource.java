package com.oreilly.com.rdf.tenuki.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.core.DatasetImpl;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.talis.tdb.bdb.BDBinstance;
import com.talis.tdb.bdb.SetupBDB;

@Path("/graphs/")
public class GraphResource {

	private Dataset dataset;

	public GraphResource() {
		String location = "tdb_bdb";
		BDBinstance bdb = new BDBinstance(location);
		DatasetGraphTDB dsg = SetupBDB.buildDataset(bdb);
		this.dataset = new DatasetImpl(dsg);
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
