package com.oreilly.rdf.tenuki;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sdb.SDB;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;

@Ignore
public class SDBBugTest {
	
	@Test
	public void testForSDBBug() throws Exception {
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql:sdb";
		String username = "sdb";
		String password = "sdb";
		Integer maxConnections = 8;
		String sdbLayout = "layout2/hash";
		String dbType = "postgresql";
		
		String queryString = "SELECT * WHERE { ?s ?p ?o} LIMIT 5";
		
		BasicDataSource dataSource = configureDataSource(driver, url,
				username, password, maxConnections);

		Connection connection = dataSource.getConnection();
		StoreDesc storeDesc = new StoreDesc(sdbLayout, dbType);
		Store store = SDBFactory.connectStore(connection, storeDesc);
		Dataset dataset = SDBFactory.connectDataset(store);
		
		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		qexec.getContext().set(SDB.unionDefaultGraph, true);
        ResultSet rs = qexec.execSelect() ;
        new SPARQLResult(rs) ;

	}
	
	private static BasicDataSource configureDataSource(String driver,
			String url, String username, String password, Integer maxConnections) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setValidationQuery("SELECT 1 AS test");
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		dataSource.setMaxActive(maxConnections);
		dataSource.setMaxIdle(maxConnections);
		if (password != null) {
			dataSource.setPassword(password);
		}
		return dataSource;
	}

}
