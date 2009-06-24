package com.oreilly.rdf.changes;

import com.hp.hpl.jena.rdf.model.Model;

public class ChangesetHandler {
	
	private Model model;

	/**
	 * @param model to apply changesets too
	 */
	public ChangesetHandler(Model model) {
		this.model = model;
	}
	
	public void applyChangeset(Changeset changeset) {
		try {
			model.begin();
			model.remove(changeset.toRemove());
			model.add(changeset.toAdd());
		} catch (RuntimeException e) {
			try {
			model.abort();
			} catch (UnsupportedOperationException e1) {
				//
			}
			throw e;
		}
		model.commit();
	}
}
