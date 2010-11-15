package com.oreilly.rdf.tenuki.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.oreilly.jaxrs.velocity.VelocityViewProcessor;
import com.oreilly.rdf.tenuki.jaxrs.io.ChangesetReader;
import com.oreilly.rdf.tenuki.jaxrs.io.ModelReader;
import com.oreilly.rdf.tenuki.jaxrs.io.ModelWriter;
import com.oreilly.rdf.tenuki.jaxrs.io.SPARQLResultWriter;

public class TenukiApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		set.add(SparqlQueryResource.class);
		set.add(GraphResource.class);
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		ModelReader modelReader = new ModelReader();
		ModelWriter modelWriter = new ModelWriter();
		ChangesetReader csReader = new ChangesetReader();
		SPARQLResultWriter sparqlWriter = new SPARQLResultWriter();
		UIResource ui = new UIResource();
		VelocityViewProcessor vvp = new VelocityViewProcessor();
		HashSet<Object> set = new HashSet<Object>();
		set.add(modelReader);
		set.add(modelWriter);
		set.add(csReader);
		set.add(sparqlWriter);
		set.add(ui);
		set.add(vvp);
		return set;
	}

}
