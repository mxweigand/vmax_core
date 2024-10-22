package com.vmax.vmax_core.server;

import org.json.JSONObject;

import com.vmax.vmax_core.api_elements.ApiDataType;
import com.vmax.vmax_core.api_helper.ApiDataTypeList;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.Node;
import com.vmax.vmax_core.graph.TriplePattern;
import com.vmax.vmax_core.graph.UnboundNode;

/**
 * <p>
 * Class to define an interface for the VMAX framework to interact with JSON objects.
 * </p>
 * <p>
 * This class provides methods to convert JSON objects to VMAX objects.
 * It also provides the static keys used in the JSON objects.
 * The conversion of VMAX objects to JSON objects is defined by the implementations of
 * {@link com.vmax.vmax_core.api_elements.ApiElement <code>ApiElement</code>}.
 * The class can be instantiated with by passing an instance of
 * {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper</code>} to the constructor.
 * </p>
 */
public class JsonInterface {

    /** key for JSON Objects to indicate the data type of a literal */
    public static final String JSON_LITERAL_DATA_TYPE_KEY = "data_type";
    /** key for JSON Objects to indicate the data of a literal */
    public static final String JSON_LITERAL_DATA_KEY = "data";
    /** key for JSON Objects to indicate the type of a node */
    public static final String JSON_TYPE_KEY = "type";
    /** key for JSON Objects to indicate the uri of a node */
    public static final String JSON_URI_KEY = "uri";
    /** keys for JSON Objects to indicate the nodes of a triple pattern */
    public static final String[] JSON_NODE_KEYS = {"subject", "predicate", "object"};

    /** Instance of ApiHelper to interact with specific elements of the API */
    private ApiHelper apiHelper;

    public JsonInterface(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    /**
     * Converts a {@link org.json.JSONObject <code>JSONObject</code>} representing a triple pattern
     * to a VMAX triple pattern ({@link com.vmax.vmax_core.graph.TriplePattern <code>TriplePattern</code>}).
     * @param triplePatternAsJson - The {@link org.json.JSONObject <code>JSONObject</code>} representing the triple pattern to be converted.
     */
    public TriplePattern createTriplePatternFromJsonData(JSONObject triplePatternAsJson) {
        // init
		Node[] nodes = new Node[3];
		// for each node in triple pattern
		for (int i = 0; i < 3; i++) {
			// try to get node data as string, if not possible -> return null
			try {
				JSONObject nodeDataAsJsonData = triplePatternAsJson.getJSONObject(JsonInterface.JSON_NODE_KEYS[i]);
                nodes[i] = this.getNodeFromJsonData(nodeDataAsJsonData);
                if (nodes[i] == null) { return null; }
			} catch (Exception e)	{
				throw new IllegalArgumentException("node data from json cannot be handled");
			}
		}
        return new TriplePattern(nodes[0], nodes[1], nodes[2]);
    }

    /**
     * Converts a {@link org.json.JSONObject <code>JSONObject</code>} representing a node
     * to a VMAX node ({@link com.vmax.vmax_core.graph.Node <code>Node</code>}).
     * @param jsonData - The {@link org.json.JSONObject <code>JSONObject</code>} representing the node to be converted.
     */
    private Node getNodeFromJsonData(JSONObject jsonData) {
        // try to get type
        String type;
        try { type = jsonData.getString(JsonInterface.JSON_TYPE_KEY); } 
        catch (Exception e) { return null; }
        // if unbound, return unbound node
        if ( type.equals(JsonNodeType.UNBOUND.toTypeString()) ) {
             return new UnboundNode();
        // else if uri 
        } else if ( type.equals(JsonNodeType.URI.toTypeString()) ) {
            // try to get uri
            String uri;
            try { uri = jsonData.getString(JsonInterface.JSON_URI_KEY); } 
            catch (Exception e) { return null; }
            // use common mehtod getNodeByUri
            return this.apiHelper.getNodeByUri(uri); 
        // else if literal
        } else if ( type.equals(JsonNodeType.LITERAL.toTypeString()) ) { 
            // try to get data type
            String dataTypeUri;
            try {
                dataTypeUri = jsonData.getString(JsonInterface.JSON_LITERAL_DATA_TYPE_KEY);
            } catch (Exception e) {
                return null;
            }
            // find matching ApiDataType
            ApiDataType foundApiDataType = ApiDataTypeList.dataTypeList.stream().filter(
                (ApiDataType apiDataType) -> (apiDataType.getUri().equals(dataTypeUri)))
                .findFirst().orElse(null);
            // if null, data type is not supported, return null
            if (foundApiDataType == null) { return null; }
            // else, use data type to create variable
            // try to get data 
            Object data;
            try {
                data = jsonData.get(JsonInterface.JSON_LITERAL_DATA_KEY);
            } catch (Exception e) {
                return null;
            }
            return foundApiDataType.convertObjectToApiEntity(data);
        // else reutrn null
        } else { return null; } 
    }

}