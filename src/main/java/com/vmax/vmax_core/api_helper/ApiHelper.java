package com.vmax.vmax_core.api_helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vmax.vmax_core.api_elements.ApiAttribute;
import com.vmax.vmax_core.api_elements.ApiClass;
import com.vmax.vmax_core.api_elements.ApiDataType;
import com.vmax.vmax_core.api_elements.ApiInstance;
import com.vmax.vmax_core.graph.RdfsHelper;
import com.vmax.vmax_core.graph.RdfsNode;
import com.vmax.vmax_core.graph.UriNode;

/**
 * <p>
 * Abstract class providing helper methods for the API.
 * </p>
 * <p>
 * The class provides methods for accessing and managing classes, instances, attributes and data types on an API.
 * Most methods are implemented in a generic manner. 
 * The class is intended to be extended by a tool-specific implementation,
 * which provides an implementation of the abstract method 
 * {@link #findApiInstanceAsObjectByUri(String uri) <code>findApiInstanceAsObjectByUri(String uri)</code>}.
 */
public abstract class ApiHelper {
   
    // lists of all classes, attributes and data types of the API
    protected List<ApiClass> classList;
    protected final List<ApiDataType> dataTypeList = ApiDataTypeList.dataTypeList;
    protected List<ApiAttribute> attributeList;
    // list of classes without superclasses ("top classes")
    protected List<ApiClass> topClassList;

    public ApiHelper(String ToolUriPrefix, ApiClassAndAttributeList apiClassAndAttributeList) {
        // get class list 
        this.classList = apiClassAndAttributeList.getClassList();
        // create class hierarchy
        this.classList.forEach((ApiClass apiClass) -> apiClass.determineSubclasses(this.classList));
        this.classList.forEach((ApiClass apiClass) -> apiClass.determineIndirectSubAndSuperclasses());
        // create list of classes without superclasses ("top classes")
        this.topClassList = this.classList.stream().filter(
            (ApiClass apiClass) -> (apiClass.getDirectSuperclasses().isEmpty()))
            .collect(Collectors.toList());
        // get attribute list
        this.attributeList = apiClassAndAttributeList.getAttributeList();
        // determine indirect source and target types for all attributes
        this.attributeList.forEach((ApiAttribute apiAttribute) -> apiAttribute.determineIndirectSourceAndTargetTypes(this.classList));
        // add source and target attributes for all classes and data types
        this.classList.forEach((ApiClass apiClass) -> apiClass.determineTargetAndSourceAttributes(this.attributeList));
        this.dataTypeList.forEach((ApiDataType apiDataType) -> apiDataType.determineTargetAndSourceAttributes(this.attributeList));
    }

    /**
     * Return a list of all attributes of the API. 
     * Used by TripleFinderHelper, TT03, TT04, TT08 and TT09.
     */
    public List<ApiAttribute> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Return a list of all classes of the API.
     * Used by TT01, TT02, TT05, TT06 and TT07.
     */
    public List<ApiClass> getClassList() {
        return this.classList;
    }

    /**
     * Return a list of all instances of the API.
     * Used by TT10 and TT11.
     */
    public List<ApiInstance> getAllInstances() {
        // init
        List<ApiInstance> allInstances = new ArrayList<ApiInstance>();
        // get all instances of all top classes
        for (ApiClass apiClass : this.topClassList) {
            List<ApiInstance> instances = apiClass.getImplementingInstances();
            if (instances != null) { allInstances.addAll(instances); }
        }
        if (allInstances.isEmpty()) { return null; }
        return allInstances.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * <p>
     * Returns a {@link com.vmax.vmax_core.graph.UriNode <code>UriNode</code>} for a given URI.
     * </p>
     * <p>
     * This methods searches for the given URI in the list of RDFS/RDFS Nodes and all elements of the API identifiable by an URI.
     * It therefore returns an instance of the following subclasses:
     * <ul>
     * <li>{@link com.vmax.vmax_core.graph.RdfsNode <code>RdfsNode</code>}</li>
     * <li>{@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}</li>
     * <li>{@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>}</li>
     * <li>{@link com.vmax.vmax_core.api_elements.ApiAttribute <code>ApiAttribute</code>}</li>
     * <li>{@link com.vmax.vmax_core.api_elements.ApiInstance <code>ApiInstance</code>}</li>
     * </ul> 
     * </p>
     * <p>
     * Returns <code>null</code> if no matching element is found.
     * </p>
     * @param uri - The URI to search for
     */
    public UriNode getNodeByUri(String uri) {
        // search for uri in rdfs nodes
        RdfsNode foundRdfsNode = RdfsHelper.getRdfsNodeByUri(uri);
        if (foundRdfsNode != null) { return foundRdfsNode; }
        // search for uri in data types
        ApiDataType foundApiDataType = this.dataTypeList.stream().filter(
            (ApiDataType apiDataType) -> (apiDataType.getUri().equals(uri)))
            .findFirst().orElse(null);
        if (foundApiDataType != null) { return foundApiDataType; }
        // search for uri in classes
        ApiClass foundApiClass = this.classList.stream().filter(
            (ApiClass apiClass ) -> (apiClass.getUri().equals(uri)))
            .findFirst().orElse(null);
        if (foundApiClass != null) { return foundApiClass; }
        // search for uri in attributes
        ApiAttribute foundApiAttribute = this.attributeList.stream().filter(
            (ApiAttribute apiAttribute) -> (apiAttribute.getUri().equals(uri)))
            .findFirst().orElse(null);
        if (foundApiAttribute != null) { return foundApiAttribute; }
        // try to get instance by uri
        // try to get instance as object from api, reuturn null on exception
        Object uncheckedObject;
        try { uncheckedObject = this.findApiInstanceAsObjectByUri(uri); } 
        catch (Exception e) { return null; }
        // get the matching api class of the object by calling the tool specific method
        ApiClass matchingApiClass = this.getApiClassOfObject(uncheckedObject);
        // if no matching class found, return null 
        if (matchingApiClass == null) { return null; }
        // else, return instance
        return matchingApiClass.convertObjectToApiEntity(uncheckedObject); 
    }
        
    /**
     * <p> 
     * Returns the {@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>} of an {@link java.lang.Object <code>Object</code>}.
     * </p>
     * <p>
     * Returns <code>null</code> if no matching class is found.
     * </p>
     * <p>
     * The method first finds all classes that the {@link java.lang.Object <code>Object</code>} is an instance of.
     * It then returns the class with the lowest number of subclasses, 
     * as this is the "lowest" class in the hierarchy of all previously found classes.
     * </p>
     * @param object - The {@link java.lang.Object <code>Object</code>} to find the {@link com.vmax.vmax_core.api_elements.ApiClass <code>ApiClass</code>} of
     */
    private ApiClass getApiClassOfObject(Object object){
        // first, get all matching classes
        List<ApiClass> matchingClasses = this.classList.stream().filter(
            (ApiClass apiClass) -> 
            (apiClass.getAssociatedClass().isInstance(object)))
            .collect(Collectors.toList());
        // find lowest class in the hierarch as all higher classes contain the lower classes as subclasses,
        ApiClass matchingClass = matchingClasses.stream()
            .min(Comparator.comparingInt((ApiClass apiClass) -> apiClass.getIndirectSubclasses().size()))
            .orElse(null);
        return matchingClass;
    };

    /**
     * Abstract method that returns an instance as an {@link java.lang.Object <code>Object</code>} from the api based on an URI.
     * This method needs to be implemented by the specific addon.
     * @param uri - The URI of the instance
     */
    protected abstract Object findApiInstanceAsObjectByUri(String uri);

}
