package com.vmax.vmax_core.api_helper;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.json.JSONObject;

import com.vmax.vmax_core.api_elements.ApiDataType;
import com.vmax.vmax_core.server.JsonInterface;

/**
 * <p>
 * Class to define a list of all data types that are supported by the API.
 * </p>
 * <p>
 * For now, the list is static and cannot be extended by the specific addon.
 * It contains of the following basic data types: 
 * <ul>
 * <li>
 * string (<code>http://www.w3.org/2001/XMLSchema#string</code>)
 * </li>
 * <li>
 * boolean (<code>http://www.w3.org/2001/XMLSchema#boolean</code>)
 * </li>
 * <li>
 * double (<code>http://www.w3.org/2001/XMLSchema#double</code>) 
 * </li>
 * <li>
 * integer (<code>http://www.w3.org/2001/XMLSchema#integer</code>) 
 * </li>
 * </ul>
 */
public class ApiDataTypeList {
    
    // define all basic data types
    public final static ApiDataType DATA_TYPE_STRING = new ApiDataType("http://www.w3.org/2001/XMLSchema#string", String.class, XSDDatatype.XSDstring) {
        @Override
        protected String getVariableDataFromObject(Object checkedObject) {
            // cast checked object to String
            return (String) checkedObject;
        }
        @Override
        public JSONObject putVariableDataOnJsonObject(JSONObject jsonObject, Object data) {
            // cast data to String
            return jsonObject.put(JsonInterface.JSON_LITERAL_DATA_KEY, (String) data);
        }
    };
    public final static ApiDataType DATA_TYPE_BOOLEAN = new ApiDataType("http://www.w3.org/2001/XMLSchema#boolean", Boolean.class, XSDDatatype.XSDboolean) {
        @Override
        protected Object getVariableDataFromObject(Object checkedObject) {
            // cast checked object to Boolean
            return (Boolean) checkedObject;
        }
        @Override
        public JSONObject putVariableDataOnJsonObject(JSONObject jsonObject, Object data) {
            // cast data to Boolean
            return jsonObject.put(JsonInterface.JSON_LITERAL_DATA_KEY, (Boolean) data);
        }
    };
    public final static ApiDataType DATA_TYPE_DOUBLE = new ApiDataType("http://www.w3.org/2001/XMLSchema#double", Double.class, XSDDatatype.XSDdouble) {
        @Override
        protected Object getVariableDataFromObject(Object checkedObject) {
            // cast checked object to Double
            return (Double) checkedObject;
        }
        @Override
        public JSONObject putVariableDataOnJsonObject(JSONObject jsonObject, Object data) {
            // cast data to Double
            return jsonObject.put(JsonInterface.JSON_LITERAL_DATA_KEY, (Double) data);
        }
    };
    public final static ApiDataType DATA_TYPE_INTEGER = new ApiDataType("http://www.w3.org/2001/XMLSchema#integer", Integer.class, XSDDatatype.XSDinteger) {
        @Override
        protected Object getVariableDataFromObject(Object checkedObject) {
            // cast checked object to Integer
            return (Integer) checkedObject;
        }
        @Override
        public JSONObject putVariableDataOnJsonObject(JSONObject jsonObject, Object data) {
            // cast data to Integer
            return jsonObject.put(JsonInterface.JSON_LITERAL_DATA_KEY, (Integer) data);
        }
    };

    /**
     * Static list of (for now) 4 basic data types (string, boolean, double, integer).
     */
    public static List<ApiDataType> dataTypeList = List.of(
        DATA_TYPE_STRING, DATA_TYPE_BOOLEAN, DATA_TYPE_DOUBLE, DATA_TYPE_INTEGER
    );

}
