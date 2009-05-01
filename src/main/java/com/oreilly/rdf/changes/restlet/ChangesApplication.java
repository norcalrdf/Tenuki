package com.oreilly.rdf.changes.restlet;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.MediaType;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.rdf.model.Model;

public class ChangesApplication extends Application {

	private DataSource datasource;
	private String datasourceType;

	@Override
	public Restlet createRoot() {
		MediaType.register("CHANGESET", "application/vnd.talis.changeset+xml");
		Router router = new Router(getContext());
		router.attach("/changes", ChangesetResource.class);
		return router;
	}

	public Model getModel() {
		IDBConnection dbcon;
		try {
			dbcon = new DBConnection(getDataSource().getConnection(),
					getDataSourceType());
			Model model = ModelRDB.open(dbcon);
			return model;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setDataSourceType(String type) {
		this.datasourceType = type;
	}

	public String getDataSourceType() {
		return this.datasourceType;
	}

	public void setDataSource(DataSource datasource) {
		this.datasource = datasource;
	}

	public DataSource getDataSource() {
		return this.datasource;
	}
}
