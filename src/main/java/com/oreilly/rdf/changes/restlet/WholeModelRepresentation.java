package com.oreilly.rdf.changes.restlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import org.restlet.data.MediaType;
import org.restlet.resource.OutputRepresentation;

import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;

public class WholeModelRepresentation extends OutputRepresentation {

	private Model model;

	public WholeModelRepresentation(Model model) {
		super(MediaType.APPLICATION_RDF_XML);
		this.model = model;
	}

	@Override
	public void write(OutputStream output) throws IOException {
		try {
			model.write(output);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try{
				model.close();
				ModelRDB model = (ModelRDB) this.model;
				model.getConnection().close();
			} catch (ClassCastException ce) {
				throw new RuntimeException("OMG NO", ce);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

	}

}
