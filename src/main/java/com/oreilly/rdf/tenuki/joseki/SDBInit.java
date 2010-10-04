package com.oreilly.rdf.tenuki.joseki;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joseki.ServerInitialization;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sdb.SDB;

public class SDBInit implements ServerInitialization{
	private Log log = LogFactory.getLog(SDBInit.class);

	@Override
	public void init(Resource service, Resource implementation) {
		log.info("Setting SDB unionDefaultGraph");
		SDB.getContext().set(SDB.unionDefaultGraph, true);		
	}
	

}
