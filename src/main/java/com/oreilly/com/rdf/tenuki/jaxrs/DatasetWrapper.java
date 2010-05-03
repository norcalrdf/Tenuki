package com.oreilly.com.rdf.tenuki.jaxrs;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sparql.core.DatasetImpl;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB;
import com.talis.tdb.bdb.BDBinstance;
import com.talis.tdb.bdb.SetupBDB;

public class DatasetWrapper {

	
	private BDBinstance bdb;
	private Dataset dataset;
	
	public DatasetWrapper(String location) {
		this.bdb = new BDBinstance(location);
		DatasetGraphTDB dsg = SetupBDB.buildDataset(bdb);
		dataset = new DatasetImpl(dsg);	
	}
	
	public Dataset getDataset() {
		return this.dataset;
	}
	
	
	public void destroy() {
		this.bdb.close();
	}
}
