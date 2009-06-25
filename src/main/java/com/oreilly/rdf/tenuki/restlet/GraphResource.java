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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDB;

public class GraphResource extends JenaModelResource {

	private String graphName;

	public GraphResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.APPLICATION_RDF_XML));
		graphName = (String) getRequest().getAttributes().get("graphName");
		try {
			graphName = URLDecoder.decode(graphName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		if (MediaType.APPLICATION_RDF_XML.equals(entity.getMediaType())) {
			Model newModel = ModelFactory.createDefaultModel();
			try {
				newModel.read(entity.getStream(), "");
			} catch (IOException e) {
				throw new ResourceException(e);
			}
			Model model = getModel(graphName);
			writeLock();
			try {
				model.removeAll();
				model.add(newModel);
				TDB.sync(model);
			} finally {
				releaseLocks();
			}
		}
		getResponse().setStatus(Status.SUCCESS_CREATED);
	}

	@Override
	public boolean allowDelete() {
		return true;
	}

	@Override
	public void removeRepresentations() throws ResourceException {
		Model model = getModel(graphName);
		writeLock();
		try {
			model.removeAll();
			model.commit();
		} finally {
			releaseLocks();
		}
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		if (MediaType.APPLICATION_RDF_XML.equals(variant.getMediaType())) {
			Model model = getModel(graphName);
			return new WholeModelRepresentation(model,getDatasetLock());
		}
		return null;
	}
}
