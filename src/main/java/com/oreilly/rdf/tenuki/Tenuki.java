package com.oreilly.rdf.tenuki;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.hp.hpl.jena.sdb.StoreDesc;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Tenuki {

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new PosixParser();

		Options options = new Options();
		options.addOption("h", "help", false, "show this message");
		options
				.addOption(OptionBuilder.withLongOpt("port").withDescription(
						"use PORT for server").hasArg().withArgName("PORT")
						.create("p"));
		options.addOption(OptionBuilder.withLongOpt("password")
				.withDescription("SQL database password").hasArg().withArgName(
						"PASSWORD").create());
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("tenuki-server", options);
				return;
			}

			System.err.println("Starting Tenuki...");
			Integer port = 7070;
			String driver = "org.postgresql.Driver";
			String url = "jdbc:postgresql:sdb";
			String username = "sdb";
			String password = null;
			if (line.getArgList().size() > 0) {
				String configFilePath = line.getArgs()[0];
				HierarchicalINIConfiguration config = new HierarchicalINIConfiguration(
						configFilePath);
				port = config.getInt("server.port", 7070);
				password = config.getString("datasource.password", password);
				driver = config.getString("datasource.driver", driver);
				username = config.getString("datasource.username", username);
				url = config.getString("datasource.url", url);
			}
			
			port = Integer.parseInt(line
					.getOptionValue("port", port.toString()));
			password = line.getOptionValue("password", password);

			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			if (password != null) {
				dataSource.setPassword(password);
			}

			StoreDesc storeDesc = new StoreDesc("layout2/index", "postgresql");

			System.err.println("... configuration complete ...");

			ServletHolder sh = new ServletHolder(ServletContainer.class);
			sh.setInitParameter("javax.ws.rs.Application",
					"com.oreilly.rdf.tenuki.jaxrs.TenukiApplication");

			Server server = new Server(port);

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
			server.join();

		} catch (ParseException e) {
			System.out.println("Unexpected exception:" + e.getMessage());
		}

	}
}
