package com.oreilly.rdf.tenuki.jaxrs.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.openjena.atlas.lib.Sink;
import org.openjena.riot.out.NQuadsWriter;
import org.openjena.riot.out.SinkQuadOutput;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

@Provider
@Produces("text/x-nquads")
public class DatasetWriter implements MessageBodyWriter<DatasetGraph> {

	@Override
	public long getSize(DatasetGraph t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return DatasetGraph.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(DatasetGraph dsg, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		Sink<Quad> sink = new SinkQuadOutput(entityStream);
		Iterator<Node> graphs = dsg.listGraphNodes();
        for ( ; graphs.hasNext() ; )
        {
        	Node graphNode = graphs.next();
        	Graph graph = dsg.getGraph(graphNode);
        	ExtendedIterator<Triple> triples = graph.find(Node.ANY, Node.ANY, Node.ANY);
        	for (; triples.hasNext() ; ) {
        		Triple triple = triples.next();
        		Quad quad = new Quad(graphNode, triple);
                sink.send(quad);
        	}
        }
        sink.close();
        entityStream.close();
	}

}
