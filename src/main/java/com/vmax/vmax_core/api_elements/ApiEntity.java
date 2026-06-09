package com.vmax.vmax_core.api_elements;

import java.util.List;

/**
 * <p>
 * Interface to represent entities of the API.
 * </p>
 * <p>
 * Entities are distinct objects that are part of the API:
 * Instances ({@link com.vmax.vmax_core.api_elements.ApiInstance <code>ApiInstance</code>})
 * or variables ({@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>}).
 * This interface introduces common properties and methods of both.
 * </p>
 */
public interface ApiEntity extends ApiElement {

    /**
     * Returns the {@link com.vmax.vmax_core.api_elements.ApiType <code>ApiType</code>} that the 
     * {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>} directly implements.
     */
    public ApiType getDirectType();

    /**
     * Returns the {@link com.vmax.vmax_core.api_elements.ApiType <code>ApiType</code>} 
     * that the {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>} directly implements
     * as well as all subtypes.
     * This practically only applies to {@link com.vmax.vmax_core.api_elements.ApiInstance <code>ApiInstance</code>}, 
     * as {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>} 
     * can only have one {@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>} that has no subtypes).
     * @return
     */
    public List<? extends ApiType> getIndirectTypes();

    /**
     * Returns the {@link java.lang.Object <code>Object</code>} that is associated 
     * with the {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>}.
     */
    public Object getEntityAsObject();

}