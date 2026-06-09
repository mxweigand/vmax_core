package com.vmax.vmax_core.graph;

import org.json.JSONObject;

import com.vmax.vmax_core.server.JsonInterface;
import com.vmax.vmax_core.server.JsonNodeType;

/**
 * <p>
 * Class for unbound nodes.
 * </p>
 * <p>
 * An unbound node is a node, that is variable and not bound to a specific value.
 * It is part of a triple pattern and can be used to represent a variable in a triple pattern query.
 * </p>
 */
public class UnboundNode implements Node {
    
    public UnboundNode() { }

    @Override
    public NodeType getNodeType() {
        return NodeType.UNBOUND;
    }

    @Override
    public org.apache.jena.graph.Node toJenaNode() {
        return org.apache.jena.graph.Node.ANY;
    }

    @Override
    public JSONObject toJson() {
        JSONObject apiElementAsJson = new JSONObject();
        apiElementAsJson.put(JsonInterface.JSON_TYPE_KEY, JsonNodeType.UNBOUND.toTypeString());
        return apiElementAsJson;
    }

    @Override
    public String toString() {
        return "<?>";
    }
    
}
