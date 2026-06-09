package com.vmax.vmax_core.graph;

import java.util.List;

/**
 * Static helper class containing the most common RDF and RDFS nodes and a method to get an RDF or RDFS node by its URI.
 */
public class RdfsHelper {
    
    // define namespaces
    private static String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static String rdfsNamespace = "http://www.w3.org/2000/01/rdf-schema#";

    // define RDF/RDFS nodes
    public static final RdfsNode RDF_TYPE = new RdfsNode(rdfNamespace + "type");
    public static final RdfsNode RDF_PROPERTY = new RdfsNode(rdfNamespace + "Property");
    public static final RdfsNode RDFS_SUBCLASSOF = new RdfsNode(rdfsNamespace + "subClassOf");
    public static final RdfsNode RDFS_DOMAIN = new RdfsNode(rdfsNamespace + "domain");
    public static final RdfsNode RDFS_RANGE = new RdfsNode(rdfsNamespace + "range");
    public static final RdfsNode RDFS_CLASS = new RdfsNode(rdfsNamespace + "Class");
    public static final RdfsNode RDFS_RESOURCE = new RdfsNode(rdfsNamespace + "Resource");
    
    // define list of all RDF/RDFS nodes
    public static final List<RdfsNode> ALL_RDFS_NODES = List.of(
        RDF_TYPE, RDF_PROPERTY, RDFS_SUBCLASSOF, RDFS_DOMAIN, RDFS_RANGE, RDFS_CLASS, RDFS_RESOURCE
    );

    /**
     * Returns the RDFS node with the given URI or <code>null</code> if no such node exists.
     * @param uri - The URI of the RDF/RDFS node to get
     */
    public static RdfsNode getRdfsNodeByUri(String uri) {
        return RdfsHelper.ALL_RDFS_NODES.stream().filter(
            (RdfsNode node) -> (node.getUri().equals(uri)))
            .findFirst().orElse(null);
    }

}
