package com.vmax.vmax_core.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.vmax.vmax_core.api_helper.ApiHelper;

/**
 * <p>
 * Class for a triple pattern server.
 * </p>
 * <p>
 * To instantiate the server, pass an implementation of the abstract class 
 * {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper<c/ode>} to the constructor.
 * </p>
 * <p>
 * The default port is <code>8080</code> and can be changed in the {@link #port <code>port</code>} field.
 * Queries can be sent using the POST method to the /triple route (e.g http://localhost:8080/triple), 
 * with the triple pattern formatted as a JSON object in the body of the request.
 * See {@link com.vmax.vmax_core.server.TriplePatternRequestHandler <code>TriplePatternRequestHandler</code>} for 
 * more details on the JSON format.
 * </p>
 */
public class TriplePatternServer extends VmaxServer {

    private final int port = 8080;

    private HttpServer httpServer;
    private final TriplePatternRequestHandler vmaxRequestHandler;

    /**
     * to implement a server for an specific add on,
     * create an implementation of the abstract class {@link ApiHelper} 
     * and pass it to this constructor
     * @param apiHelper - 
     */
    public TriplePatternServer(ApiHelper apiHelper) {
        super(apiHelper);  
        this.vmaxRequestHandler = new TriplePatternRequestHandler(this.apiHelper);
    }

    /**
     * Activates the VMAX triple pattern server.
     * @throws IOException
     */
    @Override
    public void activate() throws IOException {
        // create server
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        // create POST route to request triple patterns
        httpServer.createContext("/triple", this.vmaxRequestHandler);
        // create a default executor and start server
        httpServer.setExecutor(null); 
        httpServer.start();
    };

    /**
     * Deactivates the VMAX triple pattern server.
     */
    @Override
    public void deactivate() {
        this.httpServer.stop(0);
    }

}