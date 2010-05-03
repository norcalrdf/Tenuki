package com.oreilly.com.rdf.tenuki.jaxrs.io;

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

import com.oreilly.rdf.tenuki.Changeset;
import com.oreilly.rdf.tenuki.InputStreamChangeset;

@Provider
@Consumes("application/vnd.talis.changeset+xml")
public class ChangesetReader implements MessageBodyReader<Changeset> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// TODO: Do something useful here?
		return true;
	}

	@Override
	public Changeset readFrom(Class<Changeset> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		return new InputStreamChangeset(entityStream);
	}

}
