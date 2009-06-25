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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.hp.hpl.jena.rdf.model.Resource;

public class InputStreamChangesetTest {

	private Changeset 		changeset;

	@Before
	public void setUp() throws Exception {
		ClassPathResource changesetResource = new ClassPathResource(
		"changeset.xml");
		changeset = new InputStreamChangeset(changesetResource
		.getInputStream());

	}
	
	@Test
	public void testGetSubjectOfChange() {
		Resource subject = changeset.getSubjectOfChange();
		assertEquals("http://example.com/res#thing", subject.getURI());
	}

}
