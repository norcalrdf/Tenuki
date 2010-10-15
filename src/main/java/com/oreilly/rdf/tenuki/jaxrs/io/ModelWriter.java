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

import com.hp.hpl.jena.rdf.model.Model;

@Provider
@Produces("application/rdf+xml,text/turtle,text/plain,text/rdf+n3")
public class ModelWriter implements MessageBodyWriter<Model> {

	@Override
	public long getSize(Model t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return Model.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(Model model, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
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
		model.write(entityStream, lang);
	}

}
