package com.oreilly.rdf.changes;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public class MultiModelChangesetHandler {

	private List<Model> models;

	public MultiModelChangesetHandler(List<Model> models) {
		this.models = models;
	}

	public void applyChangeset(Changeset changeset) {
		boolean hadErrors = false;
		List<RuntimeException> exceptions = new ArrayList<RuntimeException>();
		for (Model model : models) {
			try {
				model.begin();
				model.remove(changeset.toRemove());
				model.add(changeset.toAdd());
			} catch (RuntimeException e) {
				hadErrors = true;
				exceptions.add(e);
			}
		}
		if (hadErrors) {
			for (Model model : models) {
				model.abort();
			}
			throw exceptions.get(0);
		} else {
			for (Model model : models) {
				model.commit();
			}
		}
	}

}
