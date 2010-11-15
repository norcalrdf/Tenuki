package com.oreilly.rdf.tenuki.jaxrs.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.ResultSetFormat;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;

@Provider
@Produces("application/sparql-results+xml, application/sparql-results+json, application/rdf+xml, text/turtle, text/rdf+n3, text/plain, text/csv")
public class SPARQLResultWriter implements MessageBodyWriter<SPARQLResult> {
	@Override
	public long getSize(SPARQLResult t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return SPARQLResult.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(SPARQLResult result, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		if (result.isGraph()) {
			Model model = result.getModel();
			String lang = null;
			if (mediaType
					.isCompatible(MediaType.valueOf("application/rdf+xml"))) {
				lang = "RDF/XML";
				httpHeaders.putSingle("Content-Type", "application/rdf+xml");
			}
			else if (mediaType.isCompatible(MediaType.valueOf("text/turtle"))) {
				lang = "TTL";
				httpHeaders.putSingle("Content-Type", "text/turtle");
			}
			else if (mediaType.isCompatible(MediaType.valueOf("text/rdf+n3"))) {
				lang = "N3";
				httpHeaders.putSingle("Content-Type", "text/rdf+n3");
			}
			else if (mediaType.isCompatible(MediaType.valueOf("text/plain"))) {
				lang = "N-TRIPLE";
				httpHeaders.putSingle("Content-Type", "text/plain");
			} else {
				lang = "RDF/XML";
				httpHeaders.putSingle("Content-Type", "application/rdf+xml");
			}
			model.write(entityStream, lang);
		}
		if (result.isResultSet()) {
			ResultSet rs = result.getResultSet();
			ResultSetFormat fmt = null;
			if (mediaType.isCompatible(MediaType
					.valueOf("application/sparql-results+xml"))) {
				fmt = ResultSetFormat.syntaxXML;
				httpHeaders.putSingle("Content-Type", "application/sparql-results+xml");
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/plain"))) {
				fmt = ResultSetFormat.syntaxText;
				httpHeaders.putSingle("Content-Type", "text/plain");
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/csv"))) {
				fmt = ResultSetFormat.syntaxCSV;
				httpHeaders.putSingle("Content-Type", "text/csv");
			}
			if (mediaType.isCompatible(MediaType.valueOf("application/sparql-results+json"))) {
				fmt = ResultSetFormat.syntaxJSON;
				httpHeaders.putSingle("Content-Type", "application/sparql-results+json");
			}
			ResultSetFormatter.output(entityStream, rs, fmt);
		}
	}

}
