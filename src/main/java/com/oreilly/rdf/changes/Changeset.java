package com.oreilly.rdf.changes;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public interface Changeset {
	
	public static final String CHANGESET_NS = "http://purl.org/vocab/changeset/schema#";
	
	Resource getSubjectOfChange();

	Statement[]  toRemove();

	Statement[] toAdd();

}
