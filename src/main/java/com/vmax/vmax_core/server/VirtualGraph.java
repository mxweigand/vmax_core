package com.vmax.vmax_core.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.impl.GraphBase;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.WrappedIterator;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.Triple;
import com.vmax.vmax_core.graph.TriplePattern;
import com.vmax.vmax_core.triple_finder.TripleFinder;

/**
 * <p>
 * Class for a virtual graph.
 * </p>
 * <p>
 * This class extends the {@link org.apache.jena.graph.impl.GraphBase <code>GraphBase</code>} class of the Apache Jena Framework
 * by overriding the {@link #graphBaseFind <code>graphBaseFind</code>}, which is a method that retrieves triples from the graph based on a triple pattern.
 * The overriding method redirects the triple pattern queries to a {@link com.vmax.vmax_core.triple_finder.TripleFinder <code>TripleFinder</code>}, 
 * which retrieves data from the API.
 */
public class VirtualGraph extends GraphBase {

    private final TripleFinder tripleFinder;
    private final JenaInterface jenaInterface;

    public VirtualGraph(ApiHelper apiHelper) {
        this.tripleFinder = new TripleFinder(apiHelper);
        this.jenaInterface = new JenaInterface(apiHelper);
    }

    @Override
    protected ExtendedIterator<org.apache.jena.graph.Triple> graphBaseFind(org.apache.jena.graph.Triple jenaTriplePattern) {
        // convert incoming jena triple to vmax triple pattern
        TriplePattern requestTriplePattern = this.jenaInterface.createTriplePatternFromJenaTriple(jenaTriplePattern);
        // find triples using the triple finder
        List<Triple> responseTriples = this.tripleFinder.find(requestTriplePattern);
        // init empty list of jena triples
        List<org.apache.jena.graph.Triple> responseJenaTriples = new ArrayList<org.apache.jena.graph.Triple>();
        // convert vmax triples to jena triples 
        for (Triple responseTriple : responseTriples) {
            org.apache.jena.graph.Triple jenaTriple = responseTriple.toJenaTriple();
            if (jenaTriple == null) {
                continue;
            }
            responseJenaTriples.add(jenaTriple);
        }
        // wrap list as iterator and return
        return WrappedIterator.create(responseJenaTriples.iterator());
    }
    
}
