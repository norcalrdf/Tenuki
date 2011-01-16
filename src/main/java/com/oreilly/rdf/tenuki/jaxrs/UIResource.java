package com.oreilly.rdf.tenuki.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class UIResource {

	@GET
	@Produces("text/html")
	public Viewable landingPage() {
		Context c = new VelocityContext();
		return new Viewable("/templates/index.vm", c);
	}
}
