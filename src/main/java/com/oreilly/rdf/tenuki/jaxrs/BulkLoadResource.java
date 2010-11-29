package com.oreilly.rdf.tenuki.jaxrs;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openjena.atlas.lib.Sink;
import org.openjena.riot.RiotReader;
import org.openjena.riot.lang.LangNQuads;

import com.hp.hpl.jena.sdb.store.StoreLoaderPlus;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.Quad;

@Path("/bulk")
public class BulkLoadResource extends DatasetAccessResource {
	
	
	public class SDBBulkSink implements Sink<Quad> {
		
		private StoreLoaderPlus loader;

		public SDBBulkSink(StoreLoaderPlus loader){
			this.loader = loader;
			loader.setChunkSize(100000);
			loader.startBulkUpdate();
		}

		@Override
		public void flush() {
		}

		@Override
		public void send(Quad q) {
			loader.addQuad(q.getGraph(), q.getSubject(), q.getPredicate(), q.getObject());
		}

		@Override
		public void close() {
			loader.finishBulkUpdate();
		}

	}

	@Path("")
	@POST
	@Consumes("text/x-nquads")
	public Response bulkLoad(@Context HttpServletRequest r) {
			StoreLoaderPlus loader = (StoreLoaderPlus) getStore().getLoader();
			SDBBulkSink sink = new SDBBulkSink(loader);
			try {
				LangNQuads parser = RiotReader.createParserNQuads(r.getInputStream(), sink);
				parser.parse();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			sink.close();
			return Response.noContent().build();
	}
	
	@Path("dump")
	@GET
	@Produces("text/x-nquads")
	public DatasetGraph dump() {
		return getDataset().asDatasetGraph();
	}

}
