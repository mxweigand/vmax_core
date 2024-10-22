package com.vmax.vmax_core.server;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node_ANY;

import com.vmax.vmax_core.api_elements.ApiDataType;
import com.vmax.vmax_core.api_helper.ApiDataTypeList;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.Node;
import com.vmax.vmax_core.graph.TriplePattern;
import com.vmax.vmax_core.graph.UnboundNode;

/**
 * <p>
 * Class to define an interface for the VMAX framework to interact with the Apache Jena framework.
 * </p>
 * <p>
 * This class provides methods to convert Jena objects to VMAX objects.
 * The conversion of VMAX objects to Jena objects is defined by the implementations of 
 * {@link com.vmax.vmax_core.api_elements.ApiElement <code>ApiElement</code>}.
 * The class can be instantiated with by passing an instance of 
 * {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper</code>} to the constructor.
 * </p>
 */
public class JenaInterface {
    
    /** Instance of ApiHelper to interact with specific elements of the API */
    private ApiHelper apiHelper;

    public JenaInterface(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    /**
     * Converts Jena triple pattern ({@link org.apache.jena.graph.Triple Triple}) 
     * to a VMAX triple pattern ({@link com.vmax.vmax_core.graph.TriplePattern TriplePattern}).
     * @param triplePatternAsJson - The Jena triple pattern to be converted.
     */
    public TriplePattern createTriplePatternFromJenaTriple(org.apache.jena.graph.Triple jenaTriplePattern) {
        Node subject = this.getNodeFromJenaNode(jenaTriplePattern.getSubject());
        Node predicate = this.getNodeFromJenaNode(jenaTriplePattern.getPredicate());
        Node object = this.getNodeFromJenaNode(jenaTriplePattern.getObject());
        if (subject == null | predicate == null | object == null) { return null; }
        return new TriplePattern(subject, predicate, object);
    }

    /**
     * Converts a Jena node ({@link org.apache.jena.graph.Node Node}) 
     * to a VMAX node ({@link com.vmax.vmax_core.graph.Node Node}).
     * @param jenaNode - The Jena node to be converted.
     * @return
     */
    private Node getNodeFromJenaNode(org.apache.jena.graph.Node jenaNode) {
        // if unbound, return unbound node
        // check if jenaNode is insatance of ANY
        if (jenaNode.getClass() == Node_ANY.class) {
            return new UnboundNode();
        // else if uri 
        } else if ( jenaNode.isURI() ) {
            // try to get uri
            String uri;
            try { uri = jenaNode.getURI(); } 
            catch (Exception e) { return null; }
            // getNodeByUri
            return this.apiHelper.getNodeByUri(uri);
        } else if ( jenaNode.isLiteral()) {
            // try to get data type
            RDFDatatype literalDatatype;
            try {
                literalDatatype = jenaNode.getLiteralDatatype();
            } catch (Exception e) {
                return null;
            }
            // find matching ApiDataType
            ApiDataType foundApiDataType = ApiDataTypeList.dataTypeList.stream().filter(
                (ApiDataType apiDataType) -> (apiDataType.getXsdDataType().equals(literalDatatype)))
                .findFirst().orElse(null);
            // if null, data type is not supported, return null
            if (foundApiDataType == null) { return null; }
            // else, use data type to create variable
            // try to get data 
            Object data;
            try {
                data = jenaNode.getLiteralValue();
            } catch (Exception e) {
                return null;
            }
            return foundApiDataType.convertObjectToApiEntity(data);
        // else reutrn null
        } else { return null; }  
    }

}
