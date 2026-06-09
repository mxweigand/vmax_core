package com.vmax.vmax_core.api_elements;

import java.util.Collections;
import java.util.List;

import org.apache.jena.graph.NodeFactory;
import org.json.JSONObject;

import com.vmax.vmax_core.graph.NodeType;

/**
 * <p>
 * Class to represent variables of the API.
 * </p>
 * <p>
 * A {@link com.vmax.vmax_core.api_elements.ApiVariable <code>ApiVariable</code>} is a distinct object that is part of the API 
 * and implements a {@link com.vmax.vmax_core.api_elements.ApiDataType <code>ApiDataType</code>}.
 * This class does not introduce any new methods, but implements the common methods
 * of {@link com.vmax.vmax_core.api_elements.ApiEntity <code>ApiEntity</code>}.
 * </p>
 */
public class ApiVariable implements ApiEntity {

    /** The variable as an {@link Object <code>Object</code>} */
    private final Object apiVariableAsObject;
    /** The data type of the variable */
    private final ApiDataType apiDataType;

    public ApiVariable(Object apiVariableAsObject, ApiDataType apiDataType) {
        this.apiVariableAsObject = apiVariableAsObject;
        this.apiDataType = apiDataType;
    }

    @Override
    public ApiElementType getApiElementType() {
        return ApiElementType.VARIABLE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.API_VARIABLE;
    }

    @Override
    public Object getEntityAsObject() {
        return this.apiVariableAsObject;
    }

    @Override
    public JSONObject toJson() {
        return this.apiDataType.createJsonDataFromVariable(this);
    }

    @Override
    public org.apache.jena.graph.Node toJenaNode() {
        return NodeFactory.createLiteralByValue(apiVariableAsObject, this.apiDataType.getXsdDataType());
    }

    @Override
    public ApiDataType getDirectType() {
        return this.apiDataType;
    }

    @Override
    public List<ApiDataType> getIndirectTypes() {
        return Collections.singletonList(this.apiDataType);
    }

    @Override
    public String toString() {
        return "<var: " + this.apiVariableAsObject.toString() + " of type: " + this.apiDataType.toString() + ">";
    } 

    @Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof ApiVariable)) {
            return false;
        }
        ApiVariable otherAsApiVariable = (ApiVariable) other;
        // check if data types are equal
        if ( !this.apiDataType.equals(otherAsApiVariable.getDirectType()) ) {
            return false;
        }
        // if double, compare with epsilon
        if ( this.apiVariableAsObject instanceof Double && otherAsApiVariable.getEntityAsObject() instanceof Double ) {
            return Math.abs((Double) this.apiVariableAsObject - (Double) otherAsApiVariable.getEntityAsObject()) < 0.0000001;
        // else, compare as objects
        } else {
            return this.apiVariableAsObject.equals(otherAsApiVariable.getEntityAsObject());
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + apiDataType.hashCode();
        result = 31 * result + apiVariableAsObject.hashCode();
        return result;
    }

}
