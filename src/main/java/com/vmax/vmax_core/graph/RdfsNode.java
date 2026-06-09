package com.vmax.vmax_core.graph;

/**
 * <p>
 * Class to represent an RDF or RDFS node.
 * </p>
 * <p>
 * Can be instantiated by passing the URI to the constructor.
 * </p>
 */
public class RdfsNode implements UriNode {

    private final String uri;

    public RdfsNode(String uri) {
        this.uri = uri;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RDFS;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public String toString() {
        return "<" + this.getUri() + ">";
    } 

    @Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof RdfsNode)) {
            return false;
        }
        RdfsNode otherAsRdfsNode = (RdfsNode) other;
        return this.getUri().equals(otherAsRdfsNode.getUri());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + uri.hashCode();
        return result;
    }

}
