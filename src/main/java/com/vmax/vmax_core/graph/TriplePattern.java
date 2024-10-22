package com.vmax.vmax_core.graph;

import org.json.JSONObject;

import com.vmax.vmax_core.server.JsonInterface;

/**
 * <p>
 * Class to represent a triple pattern.
 * </p>
 * <p>
 * {@link com.vmax.vmax_core.graph.Triple <code>Triples</code>} are the basic building blocks of RDF graphs.
 * This class represents a {@link com.vmax.vmax_core.graph.TriplePattern <code>TriplePattern</code>} 
 * which is a triple with one or more {@link com.vmax.vmax_core.graph.UnboundNode <code>UnboundNodes</code>}.
 * The class defines methods that allow to access data of the triple pattern and convert it into other representations.
 */
public class TriplePattern {

	private final Node subject;
	private final Node predicate;
	private final Node object;

	public TriplePattern(Node subject, Node predicate, Node object) {
		if ( subject == null ) 
			{ throw new IllegalArgumentException("triple pattern constructor: subject of a new triple pattern can't be null"); }
		if ( predicate == null ) 
			{ throw new IllegalArgumentException("triple pattern constructor: predicate of a new triple pattern can't be null"); }
		if ( object == null ) 
			{ throw new IllegalArgumentException("triple pattern constructor: object of a new triple pattern can't be null"); }
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	/**
	 * Returns the subject of the triple pattern
	 */
	public Node getSubject() {
		return subject;
	}

	/**
	 * Returns the predicate of the triple pattern
	 */
	public Node getPredicate() {
		return predicate;
	}

	/**
	 * Returns the object of the triple pattern
	 */
	public Node getObject() {
		return object;
	}

	/**
	 * Returns a JSON representation of the triple pattern
	 */
	public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonInterface.JSON_NODE_KEYS[0], this.getSubject().toJson());
        jsonObject.put(JsonInterface.JSON_NODE_KEYS[1], this.getPredicate().toJson());
        jsonObject.put(JsonInterface.JSON_NODE_KEYS[2], this.getObject().toJson());
        return jsonObject;
    }

	/**
	 * Returns the triple pattern as a {@link org.apache.jena.graph.Triple <code>org.apache.jena.graph.Triple</code>}.
	 */
	public org.apache.jena.graph.Triple toJenaTriple() {
		org.apache.jena.graph.Node subject = this.getSubject().toJenaNode();
		org.apache.jena.graph.Node predicate = this.getPredicate().toJenaNode();
		org.apache.jena.graph.Node object = this.getObject().toJenaNode();
		if (subject == null | predicate == null | object == null) { return null; }
		return org.apache.jena.graph.Triple.create(subject, predicate, object);
	}

    @Override
	public String toString() {
		return 
            "["
            + this.subject.toString() 
            + "-"
            + this.predicate.toString() 
            + "-" 
            + this.object.toString()
            + "]";
	}

	@Override
    public boolean equals(Object other) {
        if ( other == this ) { return true; }
        if ( other == null ) { return false; }
        if (!(other instanceof TriplePattern)) {
            return false;
        }
        TriplePattern otherAsTriplePattern = (TriplePattern) other;
        return this.subject.equals(otherAsTriplePattern.getSubject())
            && this.predicate.equals(otherAsTriplePattern.getPredicate())
            && this.object.equals(otherAsTriplePattern.getObject());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + subject.hashCode();
        result = 31 * result + predicate.hashCode();
        result = 31 * result + object.hashCode();
        return result;
    }

}
