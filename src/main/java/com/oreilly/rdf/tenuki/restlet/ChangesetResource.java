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
package com.oreilly.rdf.tenuki.restlet;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.oreilly.rdf.tenuki.Changeset;
import com.oreilly.rdf.tenuki.ChangesetHandler;
import com.oreilly.rdf.tenuki.InputStreamChangeset;

public class ChangesetResource extends JenaModelResource {

	private Log log = LogFactory.getLog(ChangesetResource.class);

	public ChangesetResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.valueOf("CHANGESET")));
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Model perGraphModel = null;
		try {
			Changeset changeset = new InputStreamChangeset(entity.getStream());
			perGraphModel = this.getModel(changeset.getSubjectOfChange().getURI());
			ChangesetHandler handler = new ChangesetHandler(perGraphModel);
			try {
				writeLock();
				handler.applyChangeset(changeset);
			} finally {
				releaseLocks();
			}
		} catch (IOException e) {
			throw new ResourceException(e);
		} 
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public boolean allowPut() {
		return false;
	}

}
