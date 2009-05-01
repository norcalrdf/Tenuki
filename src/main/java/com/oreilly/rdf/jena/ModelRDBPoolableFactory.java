package com.oreilly.rdf.jena;

import javax.sql.DataSource;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.ModelRDB;

public class ModelRDBPoolableFactory extends BasePoolableObjectFactory {
	private String dataSourceType;
	private DataSource dataSource;

	@Override
	public void destroyObject(Object obj) throws Exception {
		ModelRDB model = (ModelRDB) obj;
		model.getConnection().close();
		model.close();
	}

	@Override
	public void passivateObject(Object obj) throws Exception {
		ModelRDB model = (ModelRDB) obj;
		model.abort();
	}

	@Override
	public Object makeObject() throws Exception {
		DBConnection dbcon = new DBConnection(getDataSource().getConnection(),
				getDataSourceType());
		return ModelRDB.open(dbcon);
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Required
	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	@Required
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
