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
package com.oreilly.rdf.changes.restlet;

import java.util.Iterator;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.tdb.TDBFactory;

public abstract class JenaModelResource extends Resource {


	private static final String TDB_LOCATION_PARAM = "tdb.location";

	public JenaModelResource(Context content, Request request, Response responce) {
		super(content, request, responce);
	}
	
	protected void readLock() {
		getDataset().getLock().enterCriticalSection(Lock.READ);
	}
	
	protected void writeLock() {
		getDataset().getLock().enterCriticalSection(Lock.WRITE);
	}
	
	protected void releaseLocks() {
		getDataset().getLock().leaveCriticalSection();
	}
	
	protected Lock getDatasetLock() {
		return getDataset().getLock();
	}

	private Dataset getDataset() {
		String location = getContext().getParameters().getFirstValue(TDB_LOCATION_PARAM);
		Dataset set = TDBFactory.createDataset(location);
		return set;
	}
	
	public Model getDefaultModel() {
		Dataset set = getDataset();
		return set.getDefaultModel();
	}
	
	public Model getModel(String modelName) {
		return getDataset().getNamedModel(modelName);
	}
	
	public Iterator<String> modelNames() {
		return getDataset().listNames();
	}
  }
