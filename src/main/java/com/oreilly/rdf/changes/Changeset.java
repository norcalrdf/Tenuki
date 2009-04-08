package com.oreilly.rdf.changes;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public interface Changeset {
	
	public static final String CHANGESET_NS = "http://purl.org/vocab/changeset/schema#";

	Statement[]  toRemove();

	Statement[] toAdd();

}
