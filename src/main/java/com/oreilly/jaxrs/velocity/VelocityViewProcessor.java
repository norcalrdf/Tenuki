package com.oreilly.jaxrs.velocity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;

@Provider
public class VelocityViewProcessor implements ViewProcessor<Template> {

	private static Log log = LogFactory.getLog(VelocityViewProcessor.class);

	private VelocityEngine ve;

	public VelocityViewProcessor() {
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute");
		ve.setProperty("runtime.log.logsystem.log4j.logger",
				VelocityViewProcessor.class.getName());
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		try {
			ve.init();
			log.info("Velocity is loaded");
		} catch (Exception e) {
			log.error("Error when initializing Velocity", e);
		}
	}

	@Override
	public Template resolve(String name) {
		try {
			return ve.getTemplate(name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeTo(Template t, Viewable viewable, OutputStream out)
			throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(out);
		t.merge((org.apache.velocity.context.Context) viewable.getModel(),
				writer);
		writer.close();
	}

}
