package com.vmax.vmax_core.api_elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 
 * Abstract class to represent types of the API. 
 * </p>
 * <p>
 * Types are classes ({@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>}) 
 * or data types ({@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}) that are part of the API.
 * This class introduces common properties and methods of both.
 * </p>
 */
public abstract class ApiType implements ApiUriElement {

    /** uri */
    private final String uri;
    /** source attributes, i.e. attributes that have this class as source */
    protected List<ApiAttribute> sourceAttributes;
    /** target attributes, i.e. attributes that have this class as target */
    protected List<ApiAttribute> targetAttributes;
    /* the associated class of the api class */
    protected final Class<?> associatedClass;
    /** boolean to check source and target attributes have been determined */
    protected boolean attributesDetermined = false;
    /** boolean to check if indirect sub and superclasses have been determined */
    protected boolean indirectSubAndSuperclassesDetermined = false;

    public ApiType(Class<?> associatedClass, String uri) {
        this.uri = uri;
        this.associatedClass = associatedClass;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    /**
     * <p>
     * Returns the {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>} 
     * for a given {@link java.lang.Object <code>Object</code>}.
     * </p>
     * <p>
     * The {@link java.lang.Object <code>Object</code>} has to be type checked before it is passed to this method,
     * i.e. it has to be checked if it is an instance of the class that is associated with this type 
     * ({@link #associatedClass <code>associatedClass</code>}).
     * Converts a type checked Object result to an api entity
     * @param checkedObject - The type checked {@link java.lang.Object <code>Object</code>} 
     */
    public abstract ApiEntity convertObjectToApiEntity(Object checkedObject);

    public Class<?> getAssociatedClass() {
        return this.associatedClass;
    };

    /**
     * <p>
     * Returns the list of attributes with this type as source.
     * </p>
     * <p>
     * This method cannot be executed at instantiation, as attributes are defined after class and data type definition.
     * Therefore it can only be called (from externally) once attributes have been determined, i.e. after calling
     * {@link #determineTargetAndSourceAttributes <code>determineTargetAndSourceAttributes()</code>}.
     * </p>
     */
    public List<ApiAttribute> getSourceAttributes() {
        if ( !this.attributesDetermined ) 
            { throw new RuntimeException("indirect class knwowledge has not been determined yet, so the called methot wont't work"); }
        return this.sourceAttributes;
    }

    /**
     * <p>
     * Returns the list of attributes with this type as target.
     * </p>
     * <p>
     * This method cannot be executed at instantiation, as attributes are defined after class and data type definition.
     * Therefore it can only be called (from externally) once attributes have been determined, i.e. after calling
     * {@link #determineTargetAndSourceAttributes <code>determineTargetAndSourceAttributes()</code>}.
     * </p>
     */
    public List<ApiAttribute> getTargetAttributes() {
        if ( !this.attributesDetermined ) 
            { throw new RuntimeException("indirect class knwowledge has not been determined yet, so the called methot wont't work"); }
        return this.targetAttributes;
    }

    /**
     * <p>
     * Determines attributes that have this class as target or source. 
     * </p>
     * <p>
     * This method cannot be executed at instantiation, as attributes are defined after class and data type definition.
     * Therefore it has to be called from outside, as soon as all attributes are defined.
     * {@link #attributesDetermined <code>boolean attributesDetermined</code>} 
     * blocks method to be called more than once and blocks other methods to be called if this method has not been called yet.
     * </p>
     * @param apiAttributeList - List of all attributes of the API, has to be passed from outside
     */
    public void determineTargetAndSourceAttributes(List<ApiAttribute> apiAttributeList) {
        // check if attributes have already been determined and if this method has already been called
        if ( this.attributesDetermined ) 
            { throw new RuntimeException("target and source attributes have already been determined"); }
        // if this is a class, check if sub and superclasses have been determined
        if ( this.getApiElementType().equals(ApiElementType.CLASS) && !this.indirectSubAndSuperclassesDetermined ) 
            { throw new RuntimeException("target and source attributes have to be determined after sub and superclass hierarchy"); }
        // if type is a data type, set source attributes to null as only classes can be the source of attributes
        if ( this.getApiElementType().equals(ApiElementType.DATATYPE) ) { this.sourceAttributes = null; }
        // else, type is a class, so find all attributes that have this class as source or any of its subclasses as source
        else {
            this.sourceAttributes = apiAttributeList.stream()  
            .filter((ApiAttribute apiAttribute) -> apiAttribute.getIndirectSourceClasses().contains(this))
            .collect(Collectors.toList());
        }
        // find all attributes that have this class as target or any of its subclasses as target
        this.targetAttributes = apiAttributeList.stream()  
            .filter((ApiAttribute apiAttribute) -> apiAttribute.getIndirectTargetTypes().contains(this))
            .collect(Collectors.toList());
        // set booleans to true, so that this method cannot be called again and other methods can be called
        this.attributesDetermined = true;
    }

    @Override
    public String toString() {
        return "<" + this.getUri()+ ">";
    }

    @Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof ApiType)) {
            return false;
        }
        ApiType otherAsApiType = (ApiType) other;
        return this.getUri().equals(otherAsApiType.getUri());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + uri.hashCode();
        return result;
    }

}
