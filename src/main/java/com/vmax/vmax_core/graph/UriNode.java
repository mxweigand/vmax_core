package com.vmax.vmax_core.graph;

import org.apache.jena.graph.NodeFactory;
import org.json.JSONObject;

import com.vmax.vmax_core.server.JsonInterface;
import com.vmax.vmax_core.server.JsonNodeType;

/**
 * <p>
 * Interface for URI nodes.
 * </p>
 * <p>
 * A URI node is a {@link com.vmax.vmax_core.graph.BoundNode <code>BoundNode</code>}, identifiable by an URI.
 * </p>
 */
public interface UriNode extends BoundNode {

    /**
     * Returns the URI of the node.
     * Has to be implemented by the implementing class.
     */
    public String getUri();

    @Override
    default JSONObject toJson() {
        JSONObject apiElementAsJson = new JSONObject();
        apiElementAsJson.put(JsonInterface.JSON_TYPE_KEY, JsonNodeType.URI.toTypeString());
        apiElementAsJson.put(JsonInterface.JSON_URI_KEY, this.getUri());
        return apiElementAsJson;
    }

    @Override
    default org.apache.jena.graph.Node toJenaNode() {
        return NodeFactory.createURI(this.getUri());
    }

}
