package com.oreilly.rdf.tenuki.restlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RelatedGraphs extends JenaModelResource {
	private Log log = LogFactory.getLog(RelatedGraphs.class);

	private String graphName;

	public RelatedGraphs(Context content, Request request, Response responce) {
		super(content, request, responce);
		getVariants().add(new Variant(MediaType.APPLICATION_RDF_XML));
		graphName = (String) getRequest().getAttributes().get("graphName");
		try {
			graphName = URLDecoder.decode(graphName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		if (MediaType.APPLICATION_RDF_XML.equals(variant.getMediaType())) {
			Model origModel = getModel(graphName);
			Model model = ModelFactory.createDefaultModel();
			List<String> models = new ArrayList<String>();
			List<String> seen = new ArrayList<String>();
			recurse(origModel, model, models, seen);
			model.add(origModel);
			return new WholeModelRepresentation(model, getDatasetLock());
		}
		return null;
	}

	/**
	 * @param model
	 * @param combinedModel
	 * @param models
	 * @param seen
	 */
	private void recurse(Model model, Model combinedModel, List<String> models,
			List<String> seen) {
		for (StmtIterator statementIter = model.listStatements(); statementIter
				.hasNext();) {
			Statement statement = statementIter.next();
			Resource resource;
			try {
				resource = statement.getResource();
			} catch (ResourceRequiredException e) {
				break;
			}
			if (resource.isURIResource()) {
				String modelName = resource.getURI();
				log.error(modelName);
				if (!seen.contains(modelName)
						&& !models.contains(modelName)) {
					log.error(modelName);
					if (getDataset().containsNamedModel(modelName)) {
						log.error(modelName);
						models.add(modelName);
						combinedModel.add(getModel(modelName));
						recurse(model, combinedModel, models, seen);
					}
					seen.add(modelName);
				}
			}
		}
	}

}
