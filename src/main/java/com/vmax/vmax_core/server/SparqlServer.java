package com.vmax.vmax_core.server;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.sparql.core.DatasetGraphFactory;

import com.vmax.vmax_core.api_helper.ApiHelper;

/**
 * <p>
 * Class for a SPARQL server.
 * </p>
 * <p>
 * SPARQL query logic is realized by the Apache Jena Query Engine.
 * The queried graph is a {@link com.vmax.vmax_core.server.VirtualGraph <code>VirtualGraph</code>}, 
 * which redirects triple pattern queries to {@link com.vmax.vmax_core.triple_finder.TripleFinder <code>TripleFinder</code>}.
 * </p>
 * <p>
 * To instantiate the server, pass an implementation of the abstract class 
 * {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper</code>} to the constructor.
 * </p>
 * <p>
 * Query example using cURL (port 3030 can be changed in the {@link #port <code>port</code>} field):
 * </p>
 * <p>
 * <code>
 * curl http://localhost:3030/data/query -X POST --data 'query=SELECT * WHERE { ?s ?p ?o . }' -H 'Accept: text/plain'
 * </code>
 * </p>
 */
public class SparqlServer extends VmaxServer {

    private final int port = 3030;

	private final VirtualGraph graph;
	private FusekiServer server;

    public SparqlServer(ApiHelper apiHelper) {
		super(apiHelper);
		this.graph = new VirtualGraph(this.apiHelper);
    }

    /**
     * Activates the VMAX SPARQL server.
     * @throws Exception 
     */
	@Override
    public void activate() throws Exception {
        try {
            // set logging
            FusekiLogging.setLogging();
            // create and build server     
			this.server = FusekiServer
					.create()
					.port(this.port)
					.loopback(true)
					.verbose(false)
					.enablePing(true)
					.add("/data", DatasetGraphFactory.wrap(graph))
					.build();
            // start server
			this.server.start();	
		} catch (Exception e) {
			throw new Exception("error trying to start the server", e);	
		}
    };

    /**
     * Deactivates the VMAX SPARQL server.
     */
	@Override
    public void deactivate() {
        this.server.stop();
    }

}
