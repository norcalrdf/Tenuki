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
package com.oreilly.rdf.tenuki;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;

public class ChangesetHandler {
	private static Log log = LogFactory.getLog(ChangesetHandler.class);

	private Model model;

	/**
	 * @param model to apply changesets too
	 */
	public ChangesetHandler(Model model) {
		this.model = model;
	}
	
	public void applyChangeset(Changeset changeset) {
		try {
			if (model.supportsTransactions()) {
				model.begin();
			}
			Statement[] toRemove = changeset.toRemove();
			Statement[] toAdd = changeset.toAdd();
			if (log.isDebugEnabled()) {
				StringBuilder str = new StringBuilder();
				str.append("Adding: \n");
				for (int i = 0; i < toAdd.length; i++) {
					Statement statement = toAdd[i];
					str.append(statement);
				}
				str.append("Removing: \n");
				for (int i = 0; i < toRemove.length; i++) {
					Statement statement = toRemove[i];
					str.append(statement);
				}
				log.debug(str);
			}
			model.remove(toRemove);
			model.add(toAdd);
			if (model.supportsTransactions()) {
				model.commit();
			}
		} catch (RuntimeException e) {
			if (model.supportsTransactions()) {
				model.abort();
			}
		}
	}
}
