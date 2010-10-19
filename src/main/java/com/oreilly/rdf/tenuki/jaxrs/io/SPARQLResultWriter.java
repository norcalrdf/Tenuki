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
@Produces("application/sparql-results+xml, application/rdf+xml, text/turtle, text/rdf+n3, text/plain")
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

	// "application/sparql-results+xml, application/rdf+xml, text/turtle, text/rdf+n3, text/plain"

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
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/turtle"))) {
				lang = "TTL";
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/rdf+n3"))) {
				lang = "N3";
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/plain"))) {
				lang = "N-TRIPLE";
			}
			model.write(entityStream, lang);
		}
		if (result.isResultSet()) {
			ResultSet rs = result.getResultSet();
			ResultSetFormat fmt = null;
			if (mediaType.isCompatible(MediaType
					.valueOf("application/sparql-results+xml"))) {
				fmt = ResultSetFormat.syntaxXML;
			}
			if (mediaType.isCompatible(MediaType.valueOf("text/plain"))) {
				fmt = ResultSetFormat.syntaxText;
			}
			ResultSetFormatter.output(entityStream, rs, fmt);
		}
	}

}
