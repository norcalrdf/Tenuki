package com.oreilly.rdf.tenuki.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class UIResource {

	@GET
	public Viewable landingPage() {
		return new Viewable("/templates/test.vm");
	}
}
