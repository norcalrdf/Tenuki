package com.oreilly.rdf.tenuki.jaxrs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.StoreDesc;

public abstract class DatasetAccessResource {
	
	@Resource(name="jdbc/sdbDataSource", type=DataSource.class)
	private DataSource dataSource;
	
	@Resource(name="jdbc/sdbStoreDesc", type=StoreDesc.class)
	private StoreDesc storeDesc;
	
	public Dataset getDataset() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			dataSource =  (DataSource) ctx.lookup("jdbc/sdbDataSource");
			storeDesc =  (StoreDesc) ctx.lookup("jdbc/sdbStoreDesc");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		try {
			Connection connection = dataSource.getConnection();
			return SDBFactory.connectDataset(connection, storeDesc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
    @SuppressWarnings("unused")
	@PostConstruct
    private void myPostConstructMethod ()
    { 
        System.err.println("PostConstruct called");
    }
 
    @SuppressWarnings("unused")
	@PreDestroy
    private void myPreDestroyMethod ()
    {
        System.err.println("PreDestroy called");
    }



}
