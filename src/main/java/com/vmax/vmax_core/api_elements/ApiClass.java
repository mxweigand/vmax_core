package com.vmax.vmax_core.api_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vmax.vmax_core.graph.NodeType;

/**
 * <p>
 * Abstract class to represent classes of the API.
 * </p>
 * <p>
 * This class introduces common properties and methods of classes.
 * It also introduces abstract methods that have to be implemented by an API specific version of this class.
 * </p>
 */
public abstract class ApiClass extends ApiType {

    /** Direct superclasses of this class. Direct means that they are defined in the class itself. */
    protected final List<ApiClass> directSuperclasses;
    /** Indirect superclasses of this class. Indirect means that they are computed from the hierarchy of superclasses. */
    protected List<ApiClass> indirectSuperclasses;
    /** Indirect superclasses of this class, including the class itself. */
    protected List<ApiClass> indirectSuperclassesAndSelf;
    /** Direct subclasses of this class. Direct means that another class directly defines this class as its superclass. */
    protected List<ApiClass> directSubclasses;
    /** Indirect subclasses of this class. Indirect means that they are computed from the hierarchy of subclasses. */
    protected List<ApiClass> indirectSubclasses;
    /** Indirect subclasses of this class, including the class itself. */
    protected List<ApiClass> indirectSubclassesAndSelf;
    /** URI prefix for instances, passed to the constructor from externally. */
    protected String instanceUriPrefix;
    /** Boolean to check if subclasses have been determined. */
    private boolean subClassesDetermined = false;

    public ApiClass(Class<?> associatedClass, List<ApiClass> directSuperclasses, String uri, String instanceUriPrefix) {
        super(associatedClass, uri);
        this.directSuperclasses = directSuperclasses;
        this.instanceUriPrefix = instanceUriPrefix;
    }

    @Override
    public ApiElementType getApiElementType() {
        return ApiElementType.CLASS;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.API_CLASS;
    }

    @Override
    public ApiInstance convertObjectToApiEntity(Object checkedResult) {
        String uri = determineInstanceUri(checkedResult);
        return new ApiInstance(uri, this, checkedResult);
    }

    /**
     * Returns all instances ({@link com.vmax.vmax_core.api_elements.ApiInstance <code>ApiInstance</code>}) 
     * that implement this class or any of its subclasses.
     */
    public List<ApiInstance> getImplementingInstances() {
        // check if attributes have been determined
        // this method does not need attributes but should not be called before anyways
        if ( !this.attributesDetermined ) 
            { throw new RuntimeException("this method wont't work yet"); }
        // init list of instances
        List<ApiInstance> instanceList = new ArrayList<ApiInstance>();
        // get all instances as objects
        List<Object> instancesAsObjects = this.getImplementingInstancesAsObjects();
        // convert all objects to ApiInstances and add them to the list
        if ( instancesAsObjects != null && !instancesAsObjects.isEmpty() ) { 
            instanceList.addAll(instancesAsObjects.stream().map(
                (Object instanceAsObject) -> {
                    if ( instanceAsObject == null ) { return null; }
                    ApiInstance apiInstance = this.convertObjectToApiEntity(instanceAsObject);
                    if ( apiInstance == null ) { return null; }
                    return apiInstance;
                })
                .collect(Collectors.toList())); 
        }
        // if indirect instances are not already (determined by boolean indirectInstancesIncluded()), 
        // add them by repeating the process for all indirect subclasses
        if ( !this.indirectInstancesIncluded() ) {
            for (ApiClass subClass: this.indirectSubclasses) {
                instancesAsObjects = subClass.getImplementingInstancesAsObjects();
                if ( instancesAsObjects == null || instancesAsObjects.isEmpty() ) { continue; }
                instanceList.addAll(instancesAsObjects.stream().map(
                    (Object instanceAsObject) -> {
                        if ( instanceAsObject == null ) { return null; }
                        ApiInstance apiInstance = subClass.convertObjectToApiEntity(instanceAsObject);
                        if ( apiInstance == null ) { return null; }
                        return apiInstance;
                    })
                    .collect(Collectors.toList())); 
            }
        }
        if ( instanceList == null || instanceList.isEmpty() ) { return null; }
        return instanceList;
    }

    /**
     * Return the directly defined superclasses of this class
     */
    public List<ApiClass> getDirectSuperclasses() {
        return this.directSuperclasses;
    }

    /**
     * Returns all superclasses of this class, including indirect, computed superclasses.
     * Cannot be called before 
     * {@link ApiClass#determineIndirectSubAndSuperclasses <code>determineIndirectSubAndSuperclasses()</code>}
     * has been called.
     */
    public List<ApiClass> getIndirectSuperclasses() {
        // check if indirect sub and superclasses have been determined
        if ( !this.indirectSubAndSuperclassesDetermined ) 
            { throw new RuntimeException("indirect superclasses have not been determined yet, so the called method wont't work"); }
        return this.indirectSuperclasses;
    }

    /**
     * Returns the directly defined subclasses of this class.
     * Cannot be called before
     * {@link ApiClass#determineSubclasses <code>determineSubclasses()</code>}
     * has been called.
     */
    public List<ApiClass> getDirectSubclasses() {
        // check if subclasses have been determined
        if ( !this.subClassesDetermined ) 
            { throw new RuntimeException("subclasses have not been determined yet, so the called method wont't work"); }
        return this.directSubclasses;
    }

    /**
     * Returns all subclasses of this class, including indirect, computed subclasses.
     * Cannot be called before
     * {@link ApiClass#determineIndirectSubAndSuperclasses <code>determineIndirectSubAndSuperclasses()</code>}
     * has been called.
     */
    public List<ApiClass> getIndirectSubclasses() {
        // check if indirect sub and superclasses have been determined
        if ( !this.indirectSubAndSuperclassesDetermined ) 
            { throw new RuntimeException("indirect subclasses have not been determined yet, so the called method wont't work"); }
        return this.indirectSubclasses;
    }

    /**
     * Returns all subclasses of this class, including indirect, computed subclasses and the class itself.
     *  Cannot be called before
     * {@link ApiClass#determineIndirectSubAndSuperclasses <code>determineIndirectSubAndSuperclasses()</code>}
     * has been called.
     */
    public List<ApiClass> getIndirectSubclassesAndSelf() {
        // check if indirect sub and superclasses have been determined
        if ( !this.indirectSubAndSuperclassesDetermined ) 
            { throw new RuntimeException("indirect subclasses have not been determined yet, so the called method wont't work"); }
        return this.indirectSubclassesAndSelf;
    }

    /**
     * Determines direct subclasses of this class.
     * Can only be called once.
     */
    public void determineSubclasses(List<ApiClass> classList) {
        // check if method has already been called
        if ( this.subClassesDetermined ) 
            { throw new RuntimeException("subclasses have already been determined"); }
        // init list of direct subclasses
        this.directSubclasses = new ArrayList<ApiClass>();
        // determine direct/direct subclasses
        for (ApiClass apiClass : classList) {
            if ( apiClass.getDirectSuperclasses().contains(this) ) 
            { this.directSubclasses.add(apiClass); }
        }
        // set boolean to true, so that this method cannot be called again and other methods can be called
        this.subClassesDetermined = true;
    }
  
    /**
     * <p>
     * Determines indirect superclasses and subclasses of this class.
     * </p>
     * <p>
     * Method cannot be executed at instantiation, as other classes in the hierarchy might not be defined yet.
     * It therefore has to be called from outside, as soon as all classes are defined.
     * {@link ApiClass#subAndSuperClassesDetermined <code>boolean subAndSuperClassesDetermined</code>} 
     * blocks method to be called more than once and blocks other methods to be called if this method has not been called yet.
     * </p>
     */
    public void determineIndirectSubAndSuperclasses() {
        // check if subclasses have been determined
        if ( !this.subClassesDetermined ) 
            { throw new RuntimeException("subclasses have not been determined yet, so the called method wont't work"); }
        // check if method has already been called
        if ( this.indirectSubAndSuperclassesDetermined ) 
            { throw new RuntimeException("sub and superclasses have already been determined"); }
        // init list of indirect superclasses
        this.indirectSuperclasses = new ArrayList<ApiClass>();
        // add direct superclasses to indirect superclasses
        this.indirectSuperclasses.addAll(this.directSuperclasses);
        // create a list to iterate over and add direct superclasses to it in the first iteration
        List<ApiClass> currentClassLayer = this.directSuperclasses;
        // while loop to determine if the current iteration list is empty
        while (currentClassLayer.isEmpty() == false) {
            // create a list for the next iteration
            List<ApiClass> nextClassLayer = new ArrayList<ApiClass>();
            // add all the current classes direct superclasses to the next iteration list
            for (ApiClass currentClass : currentClassLayer) {
                nextClassLayer.addAll(currentClass.getDirectSuperclasses());
            }
            // add all classes in the next iteration list to the indirect superclasses list
            this.indirectSuperclasses.addAll(nextClassLayer);
            // set the current iteration list to the next iteration list
            // if the next iteration list is empty, the while loop will stop here
            currentClassLayer = nextClassLayer;
        }
        // init list of indirect subclasses
        this.indirectSubclasses = new ArrayList<ApiClass>();
        // add direct subclasses to indirect subclasses
        this.indirectSubclasses.addAll(this.directSubclasses);
        // create a list to iterate over and add direct subclasses to it in the first iteration
        currentClassLayer = this.directSubclasses;
        // while loop to determine if the current iteration list is empty
        while (currentClassLayer.isEmpty() == false) {
            // create a list for the next iteration
            List<ApiClass> nextClassLayer = new ArrayList<ApiClass>();
            // add all the current classes direct subclasses to the next iteration list
            for (ApiClass currentClass : currentClassLayer) {
                nextClassLayer.addAll(currentClass.getDirectSubclasses());
            }
            // add all classes in the next iteration list to the indirect subclasses list
            this.indirectSubclasses.addAll(nextClassLayer);
            // set the current iteration list to the next iteration list
            // if the next iteration list is empty, the while loop will stop here
            currentClassLayer = nextClassLayer;
        }
        // add self to sets of superclasses and subclasses
        this.indirectSuperclassesAndSelf = new ArrayList<ApiClass>();
        this.indirectSuperclassesAndSelf.add(this);
        this.indirectSuperclassesAndSelf.addAll(this.indirectSuperclasses);
        this.indirectSubclassesAndSelf = new ArrayList<ApiClass>();
        this.indirectSubclassesAndSelf.add(this);
        this.indirectSubclassesAndSelf.addAll(this.indirectSubclasses); 
        // set boolean to true, so that this method cannot be called again and other methods can be called
        this.indirectSubAndSuperclassesDetermined = true;
    }
    
    /**
     * Returns the URI uri of an instance supplied as an {@link java.lang.Object <code>Object</code>}.
     * This method has to be implemented by the API specific version of this class.
     * @param instanceAsObject - The instance as an {@link java.lang.Object <code>Object</code>}
     */
    protected abstract String determineInstanceUri(Object instanceAsObject);

    /**
     * Returns all instances that implement the given class.
     * This method has to be implemented by either: 
     * <ul>
     * <li>
     * An API specific version of this class, that works for all classes of the API.
     * </li>
     * <li>
     * Each class of the API individually, if the implementation of this method is different for each class.
     * </li>
     * </ul>
     */
    protected abstract List<Object> getImplementingInstancesAsObjects();

    /**
     * Returns a boolean indicating whether the method 
     * {@link ApiClass#getImplementingInstancesAsObjects <code>getImplementingInstancesAsObjects()</code>}
     * returns all instances directly implementing this class or also instances implementing subclasses of this class.
     * It needs to be implemented by the API specific version of this class.
     */
    protected abstract boolean indirectInstancesIncluded();

}
