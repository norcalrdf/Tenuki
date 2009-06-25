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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class GraphsResource extends JenaModelResource {

	private Log log = LogFactory.getLog(GraphsResource.class);

	public GraphsResource(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.TEXT_URI_LIST));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		if (MediaType.TEXT_URI_LIST.equals(variant.getMediaType())) {
			StringBuilder urilist = new StringBuilder();
			readLock();
			try {
				for (Iterator<String> iterator = modelNames(); iterator
						.hasNext();) {
					String name = iterator.next();
					urilist.append(name);
					urilist.append("\r\n");
				}
			} finally {
				releaseLocks();
			}
			return new StringRepresentation(urilist.toString(),
					MediaType.TEXT_URI_LIST);
		}
		return null;
	}

}
