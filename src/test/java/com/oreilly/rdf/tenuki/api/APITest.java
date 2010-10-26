package com.oreilly.rdf.tenuki.api;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class APITest extends JerseyTest {
	public APITest() {
		super(new WebAppDescriptor.Builder("com.oreilly.com.rdf.tenuki.jaxrs")
			.contextPath("")
			.contextParam("contextConfigLocation", "classpath:applicationContext.xml")
			.build());
	}

}
