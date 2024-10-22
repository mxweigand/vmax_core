package com.vmax.vmax_core.triple_templates;

import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;

/**
 * <p>
 * Superclass for all triple templates.
 * </p>
 * <p>
 * A triple template is a template to describe a set of similar triples, e.g. all triples defining a class:
 * </p>
 * <p>
 * <code>
 * [Class IRI] rdf:type rdfs:Class .
 * </code>
 * (this is {@link com.vmax.vmax_core.triple_templates.TT01_ClassTypeClass <code>TT01_ClassTypeClass</code>})
 * </p>
 * <p>
 * The graph consists of all triples defined by all triple templates.
 * Apart from defining the triples of the graph, triple templates have specific helper methods for triple pattern queries.
 * These methods are defined by this abstract class and implemented by the subclasses.
 * </p>
 */
public abstract class TripleTemplate {

    protected final ApiHelper apiHelper;
    protected final BoundNode predicate;

    public TripleTemplate(ApiHelper apiHelper, BoundNode predicate) {
        this.apiHelper = apiHelper;
        this.predicate = predicate;
    };

    /**
     * Returns the predicate of the triple template.
     */
    public BoundNode getPredicate() {
        return this.predicate;
    };

    /**
     * Returns all possible subjects of the triple template.
     * Calls the triple template specific method
     * {@link #getAllSubjectsSpec() <code>getAllSubjectsSpec()</code>}.
     */
    public List<? extends BoundNode> getAllSubjects() {
        return this.getAllSubjectsSpec();
    }
    
    /**
     * Returns all possible subjects of the triple template for a given object.
     * Calls the triple template specific method
     * {@link #getAllSubjectsForObjectSpec(BoundNode) <code>getAllSubjectsForObjectSpec(BoundNode object)</code>}
     * and adds some null checks.
     * @param object - The object to filter possible subjects
     */
    public List<? extends BoundNode> getAllSubjectsForObject(BoundNode object) {
        if ( !this.isApplicableForObject(object) ) { return null; }
        return this.getAllSubjectsForObjectSpec(object);
    }

    /**
     * Returns all possible objects of the triple template for a given subject.
     * Calls the triple template specific method
     * {@link #getAllObjectsForSubjectSpec(BoundNode) <code>getAllObjectsForSubjectSpec(BoundNode subject)</code>}
     * and adds some null checks.
     * @param subject - The subject to filter possible objects
     */
    public List<? extends BoundNode> getAllObjectsForSubject(BoundNode subject) {
        if ( !this.isApplicableForSubject(subject) ) { return null; }
        return this.getAllObjectsForSubjectSpec(subject);
    }

    /**
     * Determines if the triple template is applicable for a given object.
     * Calls the triple template specific method 
     * {@link #isApplicableForObjectSpec(BoundNode) <code>isApplicableForObjectSpec(BoundNode subject)</code>}
     * and adds some null checks.
     * @param object - The object to check
     * @return <code>true</code> if the triple template is applicable for the object, <code>false</code>  otherwise
     */
    public boolean isApplicableForObject(BoundNode object) {
        if ( object == null ) throw new NullPointerException("object can't be null");
        return this.isApplicableForObjectSpec(object);
    };

    /**
     * Determines if the triple template is applicable for a given subject.
     * Calls the triple template specific method
     * {@link #isApplicableForSubjectSpec(BoundNode) <code>isApplicableForSubjectSpec(BoundNode subject)</code>}
     * and adds some null checks.
     * @param subject - The subject to check
     * @return <code>true</code> if the triple template is applicable for the subject, <code>false</code>  otherwise
     */
    public boolean isApplicableForSubject(BoundNode subject) {
        if ( subject == null ) throw new NullPointerException("subject can't be null");
        return this.isApplicableForSubjectSpec(subject);
    };

    /**
     * Determines if the triple template is applicable for a given subject and object.
     * Calls the triple template specific methods
     * {@link #isApplicableForSubjectSpec(BoundNode) <code>isApplicableForSubjectSpec(BoundNode subject)</code>}
     * and
     * {@link #isApplicableForObjectSpec(BoundNode) <code>isApplicableForObjectSpec(BoundNode subject)</code>}
     * and adds some null checks.
     * @param subject - The subject to check
     * @param object - The object to check
     * @return <code>true</code> if the triple template is applicable for the subject and object, <code>false</code>  otherwise
     */
    public boolean isApplicableForSubjectAndObject(BoundNode subject, BoundNode object) {
        if ( subject == null || object == null ) throw new NullPointerException("subject/object can't be null");
        return ( this.isApplicableForSubjectSpec(subject) && this.isApplicableForObjectSpec(object));
    };

    /**
     * Abstract method to get all possible subjects of the triple template.
     * Must be implemented by the implementing triple template subclass.
     */
    public abstract List<? extends BoundNode> getAllSubjectsSpec();
    
    /**
     * Abstract method to get all possible subjects of the triple template for a given object.
     * Must be implemented by the implementing triple template subclass.
     * @param object - The object to filter possible subjects
     */
    public abstract List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object);
    
    /**
     * Abstract method to get all possible objects of the triple template for a given subject.
     * Must be implemented by the implementing triple template subclass.
     * @param subject - The subject to filter possible objects
     */
    public abstract List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject);
    
    /**
     * Abstract method to determine if the triple template is applicable for a given object.
     * Must be implemented by the implementing triple template subclass.
     * @param object - The object to check
     */
    public abstract boolean isApplicableForObjectSpec(BoundNode object);
    
    /**
     * Abstract method to determine if the triple template is applicable for a given subject.
     * Must be implemented by the implementing triple template subclass.
     * @param subject - The subject to check
     */
    public abstract boolean isApplicableForSubjectSpec(BoundNode subject);
    
}
