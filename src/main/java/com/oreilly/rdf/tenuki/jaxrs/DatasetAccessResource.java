package com.oreilly.rdf.tenuki.jaxrs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.StoreDesc;

public abstract class DatasetAccessResource {
	
	private DataSource dataSource;
	private StoreDesc storeDesc;
	private Connection connection;
	
	public Dataset getDataset() {
		return SDBFactory.connectDataset(connection, storeDesc);
	}
	
    @SuppressWarnings("unused")
	@PostConstruct
    private void myPostConstructMethod ()
    
    { 
		try {
			InitialContext ctx = new InitialContext();
			dataSource =  (DataSource) ctx.lookup("jdbc/sdbDataSource");
			storeDesc =  (StoreDesc) ctx.lookup("jdbc/sdbStoreDesc");
			connection = dataSource.getConnection();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }
 
    @SuppressWarnings("unused")
	@PreDestroy
    private void myPreDestroyMethod ()
    {
    	try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }



}
