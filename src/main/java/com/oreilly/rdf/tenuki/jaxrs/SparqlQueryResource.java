package com.oreilly.rdf.tenuki.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;

@Path("/sparql")
public class SparqlQueryResource {
	
	@Produces("application/sparql-results+xml, application/rdf+xml, text/turtle, text/rdf+n3, text/csv,text/plain")
	@GET
	@POST
	public SPARQLResult doQuary(@QueryParam("query") Query q) {
		return null;
	}

}
