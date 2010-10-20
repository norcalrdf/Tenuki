package com.oreilly.rdf.tenuki;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Tenuki {

	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		CommandLineParser parser = new PosixParser();
		
		Options options = new Options();
		options.addOption("h", "help", false, "show this message");
		options.addOption(OptionBuilder.withLongOpt("port")
				.withDescription("use PORT for server")
				.hasArg()
				.withArgName("PORT")
				.create("p") );
		try {
			CommandLine line = parser.parse( options, args );
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "tenuki-server", options );
				return;
			}

		} catch (ParseException e) {
		    System.out.println( "Unexpected exception:" + e.getMessage() );
	    }

	}
}
