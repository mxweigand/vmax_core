package com.vmax.vmax_core.server;

/**
 * Enum for different types of {@link com.vmax.vmax_core.graph.Node <code>Node</code>} 
 * for the JSON interface of the {@link com.vmax.vmax_core.server.TriplePatternServer <code>TriplePatternServer</code>}.
 */
public enum JsonNodeType {

    // options
    UNBOUND,
    URI, 
    LITERAL;

    /**
     * Maps a {@link com.vmax.vmax_core.server.JsonNodeType <code>JsonNodeType</code>} to a {@link java.lang.String <code>String</code>} 
     * that can be used in a {@link org.json.JSONObject <code>JSONObject</code>}.
     */
    public String toTypeString() {
        switch (this) {
            case UNBOUND:
                return "UNBOUND";
            case URI:
                return "URI";
            case LITERAL:
                return "LITERAL";          
        }
        return null;
    }
}
