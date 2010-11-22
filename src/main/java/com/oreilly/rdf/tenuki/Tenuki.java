package com.oreilly.rdf.tenuki;

import java.sql.Connection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;

public class Tenuki {
	private static Log log = LogFactory.getLog(Tenuki.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new PosixParser();

		Options options = new Options();
		options.addOption("h", "help", false, "show this message");
		options.addOption(OptionBuilder.withLongOpt("port").withDescription(
						"use PORT for server").hasArg().withArgName("PORT")
						.create("p"));
		options.addOption(OptionBuilder.withLongOpt("password")
				.withDescription("SQL database password").hasArg().withArgName(
						"PASSWORD").create());
		options.addOption("c", "create", false, "Create a new Datastore if one does not exist");
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("tenuki-server", options);
				return;
			}
			log.info("Starting Tenuki...");
			Integer port = 7070;
			
			String logPath = "./logs/";
			
			String driver = "org.postgresql.Driver";
			String url = "jdbc:postgresql:sdb";
			String username = "sdb";
			String password = null;
			Integer maxConnections = 8;
			String sdbLayout = "layout2/index";
			String dbType = "postgresql";
			
			if (line.getArgList().size() > 0) {
				String configFilePath = line.getArgs()[0];
				HierarchicalINIConfiguration config = new HierarchicalINIConfiguration(
						configFilePath);
				port = config.getInt("server.port", port);
				password = config.getString("datasource.password", password);
				driver = config.getString("datasource.driver", driver);
				dbType = config.getString("datasource.dbtype", dbType);
				sdbLayout = config.getString("datasource.layout", sdbLayout);
				username = config.getString("datasource.username", username);
				url = config.getString("datasource.url", url);
				maxConnections = config.getInt("datasource.maxconnections", maxConnections);
				logPath = config.getString("server.logpath", logPath);
			}
			
			port = Integer.parseInt(line
					.getOptionValue("port", port.toString()));
			password = line.getOptionValue("password", password);
			boolean create = line.hasOption("create");

			BasicDataSource dataSource = configureDataSource(driver, url,
					username, password, maxConnections);

			StoreDesc storeDesc = new StoreDesc(sdbLayout, dbType);
			log.info("... configuration complete ...");
			
			if (create) {
				Connection connection = dataSource.getConnection();
				Store store = SDBFactory.connectStore(connection, storeDesc);
				try {
					store.getSize();
				} catch (Exception e) {
					store.getTableFormatter().create();
				}
				connection.close();
			}
			
			TenukiSever server = new TenukiSever();
			server.setDatasource(dataSource);
			server.setStoreDesc(storeDesc);
			server.setLogPath(logPath);
			server.setPort(port);
			server.start();
			
			log.info("... Tenuki Server started.");

		} catch (ParseException e) {
			System.out.println("Unexpected exception:" + e.getMessage());
		}

	}

	private static BasicDataSource configureDataSource(String driver,
			String url, String username, String password, Integer maxConnections) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setValidationQuery("SELECT COUNT(*) FROM prefixes");
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		dataSource.setMaxActive(maxConnections);
		dataSource.setMaxIdle(maxConnections);
		if (password != null) {
			dataSource.setPassword(password);
		}
		return dataSource;
	}
}
