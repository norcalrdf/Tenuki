package com.oreilly.rdf.tenuki.jaxrs.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Provider
@Consumes("application/rdf+xml, text/turtle, text/plain, text/rdf+n3")
public class ModelReader implements MessageBodyReader<Model> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return Model.class.isAssignableFrom(type);
	}

	@Override
	public Model readFrom(Class<Model> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		String lang = null;
		if (mediaType.isCompatible(MediaType.valueOf("application/rdf+xml"))) {
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
		Model model = ModelFactory.createDefaultModel();
		return model.read(entityStream, "", lang);
	}

}
