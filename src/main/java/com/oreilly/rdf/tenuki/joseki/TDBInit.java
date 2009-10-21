package com.oreilly.rdf.tenuki.joseki;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joseki.ServerInitialization;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDB;

public class TDBInit implements ServerInitialization {
	private Log log = LogFactory.getLog(TDBInit.class);

	
	@Override
	public void init(Resource service, Resource implementation) {
		log.info("Setting TDB unionDefaultGraph");
		TDB.getContext().set(TDB.symUnionDefaultGraph, true);
	}

}
