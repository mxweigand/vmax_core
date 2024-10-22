package com.vmax.vmax_core.api_elements;

import com.vmax.vmax_core.graph.UriNode;

/**
 * <p>
 * Interface to represent elements that are part of the API and are identifiable by an URI.
 * </p>
 * <p>
 * This interface is used to a subset of {@link com.vmax.vmax_core.api_elements.ApiElement <code>ApiElement</code>}, 
 * but does not introduce any restrictions or additional methods.
 * </p>
 */
public interface ApiUriElement extends UriNode, ApiElement {}