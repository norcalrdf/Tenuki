package com.oreilly.rdf.changes.restlet;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;

public class GraphsResource extends JenaModelResource {
	
	private Log log = LogFactory.getLog(GraphsResource.class);

	public GraphsResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
	}

	@Override
	public boolean allowDelete() {
		return true;
	}

	@Override
	public void removeRepresentations() throws ResourceException {
		deleteAll();
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())) {
			StringBuilder urilist = new StringBuilder();
			for (String name : modelNames()) {
				urilist.append(name);
				urilist.append("\r\n");
			}
			return new StringRepresentation(urilist.toString(), MediaType.TEXT_URI_LIST);
		}
		return null;	
	}
	
	

}
