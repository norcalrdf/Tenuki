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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.data.Tag;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDB;
import com.oreilly.rdf.tenuki.Utils;

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
			updateModel(entity);
		}
		getResponse().setStatus(Status.SUCCESS_CREATED);
	}

	/**
	 * @param entity
	 * @throws ResourceException
	 */
	private void updateModel(Representation entity) throws ResourceException {
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

	@Override
	public void storeRepresentation(Representation entity)
			throws ResourceException {
		Model model = getModel(graphName);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		readLock();
		try {
			model.write(bo);
		} finally {
			releaseLocks();
		}
		Tag currentTag = Utils.calculateTag(bo.toByteArray());
		boolean doIt = false;
		for (Tag tag : getRequest().getConditions().getMatch()) {
			if (currentTag.equals(tag)) {
				doIt = true;
				break;
			}
		}
		if (doIt) {
			updateModel(entity);
		}
		getResponse().setStatus(Status.SUCCESS_OK);
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
			return new WholeModelRepresentation(model, getDatasetLock());
		}
		return null;
	}
}
