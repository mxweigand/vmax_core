package com.vmax.vmax_core.graph;

/**
 * <p>
 * Class to represent a triple.
 * </p>
 * <p>
 * A {@link com.vmax.vmax_core.graph.Triple <code>Triple</code>} resembles a {@link com.vmax.vmax_core.graph.TriplePattern <code>TriplePattern</code>} 
 * but has {@link com.vmax.vmax_core.graph.BoundNode <code>BoundNode</code>} only.
 * The class therefore extends the class {@link com.vmax.vmax_core.graph.TriplePattern <code>TriplePattern</code>} 
 * and adds a constructor that takes three {@link com.vmax.vmax_core.graph.BoundNode <code>BoundNode</code>}.
 */
public class Triple extends TriplePattern {

    public Triple(BoundNode subject, BoundNode predicate, BoundNode object) {
        super(subject, predicate, object);
    }
    
}
