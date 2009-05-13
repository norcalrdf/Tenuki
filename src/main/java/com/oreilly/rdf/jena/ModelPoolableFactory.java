package com.oreilly.rdf.jena;

import org.apache.commons.pool.PoolableObjectFactory;

import com.hp.hpl.jena.rdf.model.Model;

public interface ModelPoolableFactory extends PoolableObjectFactory {

	public abstract Model getModel(String modelName) throws Exception;

}