package com.vmax.vmax_core.api_elements;

import com.vmax.vmax_core.graph.NodeType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Class to represent instances of the API.
 * </p>
 * <p>
 * {@link com.vmax.vmax_core.api_elements.ApiInstance <code>ApiInstance</code>} 
 * are distinct objects that are part of the API and implement a
 * {@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>}.
 * This class does not introduce any new methods, but implements the common methods
 * of {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>}
 * and {@link com.vmax.vmax_core.api_elements.ApiUriElement <code>ApiUriElement</code>}.
 * </p>
 */
public class ApiInstance implements ApiUriElement, ApiEntity {

    /** The URI of the instance */
    private final String uri;
    /** The direct class of the api instance */
    private final ApiClass directApiClass;
    /** List of indirect classes of the api instance */
    private final List<ApiClass> indirectApiClasses;
    /** The instance as an {@link java.lang.Object <code>Object</code>} */
    private final Object apiInstanceAsObject;

    public ApiInstance(String uri, ApiClass directApiClass, Object apiInstanceAsObject) {
        this.uri = uri;
        this.apiInstanceAsObject = apiInstanceAsObject;
        this.directApiClass = directApiClass;
        this.indirectApiClasses = new ArrayList<ApiClass>();
        this.indirectApiClasses.add(directApiClass);
        this.indirectApiClasses.addAll(this.directApiClass.getIndirectSuperclasses());
    }

    @Override
    public ApiElementType getApiElementType() {
        return ApiElementType.INSTANCE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.API_INSTANCE;
    }

    @Override
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public Object getEntityAsObject() {
        return apiInstanceAsObject;
    }

    @Override
    public ApiClass getDirectType() {
        return directApiClass;
    }

    @Override
    public List<ApiClass> getIndirectTypes() {
        return indirectApiClasses;
    }
    
    @Override
    public String toString() {
        return "<" + this.getUri()+ ">";
    } 

    @Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof ApiInstance)) {
            return false;
        }
        ApiInstance otherAsApiInstance = (ApiInstance) other;
        return this.getUri().equals(otherAsApiInstance.getUri());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + uri.hashCode();
        return result;
    }

}
