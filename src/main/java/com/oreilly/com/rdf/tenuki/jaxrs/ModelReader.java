package com.oreilly.com.rdf.tenuki.jaxrs;

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
@Consumes("application/rdf+xml,text/turtle,text/plain,text/rdf+n3")
public class ModelReader implements MessageBodyReader<Model> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		//TODO: Figure out what the heck to do here.
		return true;
	}

	@Override
	public Model readFrom(Class<Model> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		Model model = ModelFactory.createDefaultModel();
		return model.read(entityStream, "");
	}

}
