package com.vmax.vmax_core.graph;

import org.json.JSONObject;

/**
 * <p>
 * Interface for the nodes of the graph. 
 * </p>
 * <p>
 * This interface defines general methods, implementing classes have to provide.
 * A node can be bound/determined ({@link com.vmax.vmax_core.graph.BoundNode <code>BoundNode</code>}) or undetermined/unbound ({@link com.vmax.vmax_core.graph.UnboundNode <code>UnboundNode</code>}).
 * </p>
 */
public interface Node {

    /**
     * Returns the type of the node defined by the enum {@link com.vmax.vmax_core.graph.NodeType <code>NodeType</code>}
     */
    public NodeType getNodeType();

    /**
     * Converts the node to a {@link org.json.JSONObject <code>JSONObject</code>}, that can be used for the communication with the 
     * {@link com.vmax.vmax_core.server.TriplePatternServer <code>TriplePatternServer</code>}.
     */
    public JSONObject toJson();

    /**
     * Converts the node to a {@link org.apache.jena.graph.Node <code>org.apache.jena.graph.Node</code>}, that can be used by the 
     * {@link com.vmax.vmax_core.graph.VirtualGraph <code>VirtualGraph</code>}.
     */
    public org.apache.jena.graph.Node toJenaNode();

    /**
     * Returns a string representation of the node.
     * This method should be overridden by all implementing classes.
     */
    @Override
    public String toString();

}