package com.oreilly.rdf.tenuki.api;

import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public abstract class APITest extends JerseyTest {
	public APITest() {
		super(new WebAppDescriptor.Builder("com.oreilly.com.rdf.tenuki.jaxrs")
			.contextPath("")
			.contextParam("contextConfigLocation", "classpath:applicationContext.xml")
			.servletClass(SpringServlet.class)
			.contextListenerClass(ContextLoaderListener.class)
			.build());
	}

}
