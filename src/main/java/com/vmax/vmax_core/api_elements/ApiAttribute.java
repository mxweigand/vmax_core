package com.vmax.vmax_core.api_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;

/**
 * <p>
 * Class to represent attributes of the API.
 * </p>
 * <p>
 * An {@link com.vmax.vmax_core.api_elements.ApiAttribute <code>ApiAttribute</code>} is an attribute of a 
 * {@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>}.
 * This is usually a method or a field of a java class.
 * We call this class the source class of the attribute.
 * The target type of the attribute is the return type of the method or the type of the field.
 * This can be an {@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>} 
 * or an {@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}.
 * </p>
 */
public abstract class ApiAttribute implements ApiUriElement {
    
    /** The URI of the attribute */
    private final String uri;
    /** The {@link ApiClass <code>ApiClass</code>} that is the source of the attribute */
    private final ApiClass sourceClass;
    /** The {@link ApiType <code>ApiType</code>} that is the target of the attribute */
    private final ApiType targetType;
    /** List of indirect source classes of the attribute */
    private List<ApiClass> indirectSourceClasses;
    /** List of indirect target types of the attribute */
    private List<ApiType> indirectTargetTypes;
    /** Boolean to determine if indirect source and target types have been determined */
    private boolean indirectSourceAndTargetTypesDetermined = false;

    public ApiAttribute(ApiClass sourceClass, ApiType targetType, String uri)  {
        this.uri = uri;
        this.sourceClass = sourceClass;
        this.targetType = targetType;
    }

    @Override
    public ApiElementType getApiElementType() {
        return ApiElementType.ATTRIBUTE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.API_ATTRIBUTE;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    /**
     * Returns the {@link ApiClass <code>ApiClass</code>} that is the source of this attribute.
     */
    public ApiClass getSourceClass() {
        return this.sourceClass;
    }

    /**
     * Returns the {@link ApiClass <code>ApiClass</code>} that is the source of this attribute as well as all subclasses of the source class.
     */
    public List<ApiClass> getIndirectSourceClasses() {
        // check if indirect source and target types have been determined
        if ( !this.indirectSourceAndTargetTypesDetermined )
            { throw new RuntimeException("source and target types have not been determined yet"); }
        return this.indirectSourceClasses;
    }

    /**
     * Returns the {@link ApiType <code>ApiType</code>} that is the target of this attribute.
     */
    public ApiType getTargetType() {
        return this.targetType;
    }

    /**
     * Returns the {@link ApiType <code>ApiType</code>} that is the target of this attribute 
     * as well as all subclasses of the target type (only if target type is a class).
     * @return
     */
    public List<ApiType> getIndirectTargetTypes() {
        // check if indirect source and target types have been determined
        if ( !this.indirectSourceAndTargetTypesDetermined )
            { throw new RuntimeException("source and target types have not been determined yet"); }
        return this.indirectTargetTypes;
    }

    /**
     * <p>
     * Returns a list of {@link ApiEntity <code>ApiEntity</code>} that are the target of the attribute
     * for a given source {@link ApiInstance <code>ApiInstance</code>}.
     * </p>
     * <p>
     * Executes the app specific method {@link #getTargetEntitiesForSourceInstanceSpec(Object sourceInstanceObject)} 
     * on the given source {@link ApiInstance <code>ApiInstance</code>} and checks returned data etc. 
     * </p>
     * @param sourceInstance - The source {@link ApiInstance <code>ApiInstance</code>} 
     * for which the target {@link ApiEntity <code>ApiEntity</code>} should be determined
     */
    public List<? extends BoundNode> getTargetEntitiesForSourceInstance(ApiInstance sourceInstance) {
        // check if indirect source and target types have been determined
        if ( !this.indirectSourceAndTargetTypesDetermined )
            { throw new RuntimeException("source and target types have not been determined yet"); }
        // get source instance as app specific object
        Object sourceInstanceAppSecific = sourceInstance.getEntityAsObject();
        // try to call app specific function
        List<? extends Object> uncheckedResultList;
		try { 
            uncheckedResultList = getTargetEntitiesForSourceInstanceSpec(sourceInstanceAppSecific);
        // if app specific function cannot be called, return null
        } catch (Exception e) {
            return null;
        }
        // return null if result is null or empty list
        if ( uncheckedResultList == null || uncheckedResultList.isEmpty() ) { return null; }
        // filter list for objects of correct class
        List<? extends Object> checkedResultList = uncheckedResultList.stream()
            .filter((Object singleResult) -> (singleResult != null))
            .filter((Object singleResult) -> this.targetType.getAssociatedClass().isInstance(singleResult))
            .collect(Collectors.toList());
        // if result list is empty or null return null
        if (checkedResultList == null || checkedResultList.isEmpty()) { return null; }
        // else, convert each elelement and return list
        else { 
            return checkedResultList.stream()
                .filter((Object singleResult) -> (singleResult != null))
                .map((Object singleResult) -> this.targetType.convertObjectToApiEntity(singleResult))
                .collect(Collectors.toList());
        }
    }
    
    /**
     * <p>
     * Returns a list of {@link java.lang.Object <code>Object</code>} that are the target of the attribute
     * for a given source {@link java.lang.Object <code>Object</code>}.
     * </p>
     * <p>
     * This is the API specific method to get target entities for a source instance.
     * It must be implemented by the API specific subclasses representing the attributes of the API.
     * @param sourceInstanceObject - The source instance passed as an {@link java.lang.Object <code>Object</code>}
     */
    public abstract List<? extends Object> getTargetEntitiesForSourceInstanceSpec(Object sourceInstanceObject);

    /**
     * <p>
     * Determines the indirect source classes and target types of the attribute.
     * </p>
     * <p>
     * This method cannot be called during the construction of the attribute,
     * as subclasses and superclass of the source class and target type might not be known yet.
     * It has to be called from outside after all classes and types have been added to the API 
     * and subclasses and superclasses have been determined.
     * </p>
     * @param classList - The list of all {@link ApiClass <code>ApiClass</code>} of the API
     */
    public void determineIndirectSourceAndTargetTypes(List<ApiClass> classList) {
        // check if method has been executed already
        if ( this.indirectSourceAndTargetTypesDetermined )
            { throw new RuntimeException("source and target types have already been determined"); }
        // indirect source classes: list of direct source class and all subclasses of direct source class
        indirectSourceClasses = new ArrayList<ApiClass>();
        indirectSourceClasses.add(this.sourceClass);
        indirectSourceClasses.addAll(this.sourceClass.getIndirectSubclasses());
        // indirect target types: list of direct target type and all subclasses of direct target type 
        // only if target type is a class - else indirect target types is just list with a single type 
        indirectTargetTypes = new ArrayList<ApiType>();
        indirectTargetTypes.add(this.targetType);
        if ( this.targetType.getApiElementType().equals(ApiElementType.CLASS) ) 
            { indirectTargetTypes.addAll(((ApiClass) this.targetType).getIndirectSubclasses()); }
        // set boolean to true
        this.indirectSourceAndTargetTypesDetermined = true;
    }
    
    @Override
    public String toString() {
        return "<" + this.getUri()+ ">";
    } 

    @Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof ApiAttribute)) {
            return false;
        }
        ApiAttribute otherAsApiAttribute = (ApiAttribute) other;
        return this.getUri().equals(otherAsApiAttribute.getUri());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + uri.hashCode();
        return result;
    }

}