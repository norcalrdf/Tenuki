/*   
 * Copyright 2009 O'Reilly Media, Inc
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.oreilly.rdf.changes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Model;

public class MultiModelChangesetHandler {
	private Log log = LogFactory.getLog(MultiModelChangesetHandler.class);

	private List<Model> models;

	public MultiModelChangesetHandler(List<Model> models) {
		this.models = models;
	}

	public void applyChangeset(Changeset changeset) {
		boolean hadErrors = false;
		List<RuntimeException> exceptions = new ArrayList<RuntimeException>();
		for (Model model : models) {
			try {
				log.trace("Applying changeset");
				model.begin();
				model.remove(changeset.toRemove());
				model.commit();
				model.add(changeset.toAdd());
				log.info("Commiting changeset");
				model.commit();
			} catch (RuntimeException e) {
				log.error("While processing changeset: ", e);
				log.info("Rolling back changset");
				model.abort();
				hadErrors = true;
				exceptions.add(e);
			}
		}
		if (hadErrors) {
			throw exceptions.get(0);
		}
	}

}
