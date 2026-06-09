package com.vmax.vmax_core.api_elements;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.json.JSONObject;

import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.server.JsonInterface;
import com.vmax.vmax_core.server.JsonNodeType;

/**
 * <p>
 * Abstract class to represent data types of the API.
 * </p>
 * <p>
 * This class introduces common properties and methods of classes.
 * It also introduces abstract methods that have to be implemented by each data type individually.
 * All data types are defined in the data type list 
 * ({@link com.vmax.vmax_core.api_helper.ApiDataTypeList <code>ApiDataTypeList</code>}).
 * </p>
 */
public abstract class ApiDataType extends ApiType {

    /** the XSD data type of the data type for communication with the Apache Jena Framework */
    private final XSDDatatype xsdDataType;

    public ApiDataType(String uri, Class<?> associatedClass, XSDDatatype xsdDataType) {
        super(associatedClass, uri);
        this.xsdDataType = xsdDataType;
        // set the boolean to true, as there are no sub and superdatatypes
        indirectSubAndSuperclassesDetermined = true;
    }

    @Override
    public ApiElementType getApiElementType() {
        return ApiElementType.DATATYPE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.API_DATATYPE;
    }
    
    /**
     * Returns the XSD data type of the data type for communication with the Apache Jena Framework.
     * @return
     */
    public XSDDatatype getXsdDataType() {
        return this.xsdDataType;
    }

    /**
     * <p>
     * Returns a JSON representation ({@link org.json.JSONObject <code>JSONObject</code>}) of an 
     * {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>}
     * that has this data type.
     * </p>
     * @param apiVariable - The 
     * {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>} 
     * that should be represented as a
     * {@link org.json.JSONObject <code>JSONObject</code>}
     */
    public JSONObject createJsonDataFromVariable(ApiVariable apiVariable) {
        // create a new json object for the variable
        JSONObject apiVariableAsJson = new JSONObject();
        // add the type of the variable
        apiVariableAsJson.put(JsonInterface.JSON_TYPE_KEY, JsonNodeType.LITERAL.toTypeString());
        // add the data type of the variable
        apiVariableAsJson.put(JsonInterface.JSON_LITERAL_DATA_TYPE_KEY, this.getUri());
        // add the data of the variable using the abstract method and return the json object
        return this.putVariableDataOnJsonObject(apiVariableAsJson, apiVariable.getEntityAsObject());
    }

    /**
     * <p>
     * Adds the data of a variable to a predefined JSON object.
     * </p>
     * <p>
     * While the method {@link #createJsonDataFromVariable <code>createJsonDataFromVariable(ApiVariable)</code>}
     * creates a new {@link org.json.JSONObject <code>JSONObject</code>} for a given
     * {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>}
     * and adds the node type and data type information to it,
     * this method this actually adds the data of the 
     * {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>}
     * to the predefined JSON object.
     * This method has to be defined by each extending subclass of 
     * {@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}
     * individually and is therefore an abstract method.
     * </p>
     * @param jsonObject - The predefined {@link org.json.JSONObject <code>JSONObject</code>} 
     * to which the data should be added
     * @param data - The data of the variable given as an {@link java.lang.Object <code>Object</code>}.
     * The extending subclass should cast this object to the correct class representing the data type.
     */
    public abstract JSONObject putVariableDataOnJsonObject(JSONObject jsonObject, Object data);

    @Override
    public ApiVariable convertObjectToApiEntity(Object checkedResult) {
        return new ApiVariable(this.getVariableDataFromObject(checkedResult), this);
    }

    /**
     * <p>
     * Returns the data for a variable as an {@link java.lang.Object <code>Object</code>} from a given {@link java.lang.Object <code>Object</code>}.
     * </p>
     * <p>
     * This method is used to convert the data for a variable to an {@link java.lang.Object <code>Object</code>}. 
     * Implementing sublclasses should however convert into a more specific class. 
     * The method therefore has to be defined by each extending subclass of
     * {@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}
     * individually.
     * </p>
     * @param checkedResult - The given {@link java.lang.Object <code>Object</code>} from which the data should be extracted.
     */
    protected abstract Object getVariableDataFromObject(Object checkedResult);

}
