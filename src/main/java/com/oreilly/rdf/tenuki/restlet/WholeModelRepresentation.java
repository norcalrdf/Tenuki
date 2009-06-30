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
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.data.MediaType;
import org.restlet.resource.OutputRepresentation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.Lock;

public class WholeModelRepresentation extends OutputRepresentation {
	private Log log = LogFactory.getLog(WholeModelRepresentation.class);
	private Model model;
	private Lock lock;

	public WholeModelRepresentation(Model model, Lock lock) {
		super(MediaType.APPLICATION_RDF_XML);
		this.model = model;
		this.lock = lock;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		lock.enterCriticalSection(Lock.READ);
		try {
			model.write(output);
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		} finally {
			lock.leaveCriticalSection();
		}

	}

}