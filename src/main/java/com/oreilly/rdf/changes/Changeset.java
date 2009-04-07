package com.oreilly.rdf.changes;

import com.hp.hpl.jena.rdf.model.StmtIterator;

public interface Changeset {

	StmtIterator toRemove();

	StmtIterator toAdd();

}
