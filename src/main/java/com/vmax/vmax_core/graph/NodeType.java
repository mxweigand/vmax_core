package com.vmax.vmax_core.graph;

import com.vmax.vmax_core.server.JsonNodeType;

/**
 * Enum for different types of {@link com.vmax.vmax_core.graph.Node <code>Node</code>}.
 */
public enum NodeType {

    // options
    UNBOUND,
    RDFS,
    API_CLASS,
    API_DATATYPE,
    API_INSTANCE,
    API_VARIABLE,
    API_ATTRIBUTE;

    /**
     * Maps a {@link com.vmax.vmax_core.graph.NodeType <code>NodeType</code>}to a {@link com.vmax.vmax_core.server.JsonNodeType <code>JsonNodeType</code>}.
     */
    public JsonNodeType toJsonNodeType() {
        switch (this) {
            case UNBOUND:
                return JsonNodeType.UNBOUND;
            case RDFS:
            case API_CLASS:
            case API_DATATYPE:
            case API_INSTANCE:
            case API_ATTRIBUTE:
                return JsonNodeType.URI;
            case API_VARIABLE:
                return JsonNodeType.LITERAL;           
        }
        return null;
    }
}
