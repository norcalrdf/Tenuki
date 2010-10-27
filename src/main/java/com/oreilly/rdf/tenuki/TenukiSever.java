package com.oreilly.rdf.tenuki;

import javax.sql.DataSource;

import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.hp.hpl.jena.sdb.StoreDesc;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class TenukiSever {
	
	private Integer port;
	private DataSource dataSource;
	private StoreDesc storeDesc;
	private Server server;
	
	public void setPort(Integer port) {
		this.port = port;
	}

	public void setDatasource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setStoreDesc(StoreDesc storeDesc) {
		this.storeDesc = storeDesc;
	}
	
	public void start() throws Exception {
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("javax.ws.rs.Application",
				"com.oreilly.rdf.tenuki.jaxrs.TenukiApplication");

		server = new Server(port);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setConfigurationClasses(new String[] {
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration" });

		webAppContext.setContextPath("/");
		webAppContext.addServlet(sh, "/*");

		new Resource("jdbc/sdbDataSource", dataSource);
		new Resource("jdbc/sdbStoreDesc", storeDesc);

		server.setHandler(webAppContext);
		server.start();
	}
	
	public void stop() throws Exception {
		server.stop();
	}

}
