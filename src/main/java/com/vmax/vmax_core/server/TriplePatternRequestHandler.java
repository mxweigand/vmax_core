package com.vmax.vmax_core.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.Triple;
import com.vmax.vmax_core.graph.TriplePattern;
import com.vmax.vmax_core.triple_finder.TripleFinder;

/**
 * <p>
 * Class for a triple pattern request handler.
 * </p>
 * <p>
 * Handles incoming POST requests.
 * Converts the incoming stream to a JSON object, and redirects it to the {@link com.vmax.vmax_core.triple_finder.TripleFinder <code>TripleFinder</code>}.
 * </p>
 */
public class TriplePatternRequestHandler implements HttpHandler {

    private final TripleFinder tripleFinder;
    private final JsonInterface jsonInterface;

    public TriplePatternRequestHandler(ApiHelper apiHelper) {
        this.tripleFinder = new TripleFinder(apiHelper);
        this.jsonInterface = new JsonInterface(apiHelper);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // reject (code 405) if any other method than POST is chosen    
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
        }
        // read input stream to get request as string
        InputStreamReader inputStreamReader =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int b;
        StringBuilder stringBuilder = new StringBuilder(512);
        while ((b = bufferedReader.read()) != -1) 
            { stringBuilder.append((char) b); }
        String requestString = stringBuilder.toString();
        // close 
        bufferedReader.close();
        inputStreamReader.close();
        // convert request to JSON object
        JSONObject requestAsJson = new JSONObject(requestString);
        // convert JSON object to triple pattern
        TriplePattern requestTriplePattern = this.jsonInterface.createTriplePatternFromJsonData(requestAsJson);
        // get matching triples from triple finder
        List<Triple> responseTriples = this.tripleFinder.find(requestTriplePattern);
        // convert triples to JSON array
        JSONArray responseAsJson = new JSONArray();
        String responseString;
        // check if response is not null and not empty
        // if so, convert each triple to JSON and put it on response array
        if ( responseTriples != null && !responseTriples.isEmpty() ) {
            for (Triple responseTriple : responseTriples) { 
                responseAsJson.put(responseTriple.toJson()); 
            }
            responseString = responseAsJson.toString();
        // if empty or null, set responseString to "[]" to return an empty JSON array
        } else {
            responseString = "[]";
        }
        // send response with code 200
        exchange.sendResponseHeaders(200, responseString.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(responseString.getBytes());
        output.flush();
    }

}