package com.oreilly.rdf.tenuki.jaxrs;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;

@Path("/sparql")
public class SparqlQueryResource {
	
	private Dataset dataset;

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	
	@Produces("application/sparql-results+xml, application/rdf+xml, text/turtle, text/rdf+n3, text/csv,text/plain")
	@GET
	public SPARQLResult getQuery(@QueryParam("query") String queryString) {
		return doQuery(queryString);
	}

	@Produces("application/sparql-results+xml, application/rdf+xml, text/turtle, text/rdf+n3, text/csv,text/plain")
	@POST
	public SPARQLResult postQuery(@QueryParam("query") String queryString) {
		return doQuery(queryString);
	}


	private SPARQLResult doQuery(String queryString) {
		Query query = null;
		try {
			query = QueryFactory.create(queryString, Syntax.syntaxARQ);
		} catch (QueryException e) {
			throw e;
		}
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
        if ( query.isSelectType() )
        {
            ResultSet rs = qexec.execSelect() ;
            return new SPARQLResult(rs) ;
        }
        if ( query.isConstructType() )
        {
            Model model = qexec.execConstruct() ;
            return new SPARQLResult(model) ;
        }

        if ( query.isDescribeType() )
        {
            Model model = qexec.execDescribe() ;
            return new SPARQLResult(model) ;
        }

        if ( query.isAskType() )
        {
            boolean b = qexec.execAsk() ;
            return new SPARQLResult(b) ;
        }
        return null;
	}

}
