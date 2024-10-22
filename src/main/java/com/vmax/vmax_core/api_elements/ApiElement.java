package com.vmax.vmax_core.api_elements;

import com.vmax.vmax_core.graph.BoundNode;

/**
 * <p>
 * Interface to represent elements that are part of the API.
 * </p>
 * <p>
 * Introduces the method {@link #getApiElementType() <code>getApiElementType()</code>} that returns the type of the element.
 * </p>
 */
public interface ApiElement extends BoundNode {

    /**
     * Returns the type of the element as defined by the enum {@link com.vmax.vmax_core.api_elements.ApiElementType <code>ApiElementType</code>}.
     */
    public ApiElementType getApiElementType();
    
}
