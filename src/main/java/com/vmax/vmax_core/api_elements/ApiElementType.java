package com.vmax.vmax_core.api_elements;

import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.server.JsonNodeType;

/**
 * <p>
 * Enum to represent differnt types of {@link ApiElement <code>ApiElement</code>}.
 * </p>
 */
public enum ApiElementType {

    // options
    CLASS,
    DATATYPE,
    INSTANCE,
    VARIABLE,
    ATTRIBUTE;

    /**
     * Maps an {@link ApiElementType <code>ApiElementType</code>} 
     * to a {@link com.vmax.vmax_core.graph.NodeType <code>NodeType</code>}.
     */
    public NodeType toNodeType() {
        switch (this) {
            case CLASS:
                return NodeType.API_CLASS;
            case ATTRIBUTE:
                return NodeType.API_ATTRIBUTE;
            case DATATYPE:
                return NodeType.API_DATATYPE;
            case INSTANCE:
                return NodeType.API_INSTANCE;
            case VARIABLE:
                return NodeType.API_VARIABLE;            
        }
        return null;
    }

    /**
     * Maps an {@link ApiElementType <code>ApiElementType</code>} 
     * to a {@link com.vmax.vmax_core.server.JsonNodeType <code>JsonNodeType</code>}.
     * @return
     */
    public JsonNodeType toJsonNodeType() {
        switch (this) {
            case CLASS:
            case ATTRIBUTE:
            case DATATYPE:
            case INSTANCE:
                return JsonNodeType.URI;
            case VARIABLE:
                return JsonNodeType.LITERAL;
        }
        return null;
    }

}
