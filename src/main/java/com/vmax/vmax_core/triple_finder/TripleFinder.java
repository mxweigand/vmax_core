package com.vmax.vmax_core.triple_finder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.Node;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.Triple;
import com.vmax.vmax_core.graph.TriplePattern;

/**
 * <p>
 * Class for a triple finder to find {@link com.vmax.vmax_core.graph.Triple <code>Triples</code>} based on a {@link com.vmax.vmax_core.graph.TriplePattern <code>TriplePattern</code>}.
 * </p>
 * <p>
 * To instantiate the triple finder, an implementation of the abstract class 
 * {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper</code>} is passed to the constructor.
 * This is not done directly, but via an extension of the abstract class {@link com.vmax.vmax_core.server.VmaxServer <code>VmaxServer</code>}.
 * </p>
 */
public class TripleFinder {
    
    private TripleFinderHelper tripleFinderHelper;
    
    public TripleFinder(ApiHelper apiHelper) {
        this.tripleFinderHelper = new TripleFinderHelper(apiHelper);
    }

    public List<Triple> find(TriplePattern inputTriplePattern) {
        // initialize empty list of resulting triples
        List<Triple> resultList = new ArrayList<Triple>();
        // get nodes from triple
        Node inputSubject = inputTriplePattern.getSubject();
        Node inputPredicate = inputTriplePattern.getPredicate();
        Node inputObject = inputTriplePattern.getObject();
        // find out type of triple pattern (i.e. combination of bound and unbound nodes)
        if ( inputSubject.getNodeType() == NodeType.UNBOUND) {
            if ( inputPredicate.getNodeType() == NodeType.UNBOUND) {
                if ( inputObject.getNodeType() == NodeType.UNBOUND) {
                    // query logic for triples of type <?s ?p ?o>
                    // get all predicates
                    List<BoundNode> predicateList = this.tripleFinderHelper.getAllPredicates();
                    if ( predicateList == null || predicateList.isEmpty() ) { return null; }
                    // for each predicate, get all subjects
                    for (BoundNode predicate: predicateList) {
                        List<BoundNode> subjectList = this.tripleFinderHelper.getAllSubjectsForPredicate(predicate);
                        if ( subjectList == null || subjectList.isEmpty() ) { continue; }
                        // for each subject of each predicate, get all objects
                        for (BoundNode subject: subjectList) {
                            List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject(predicate, subject);
                            if ( objectList == null || objectList.isEmpty() ) { continue; }
                            for (BoundNode object: objectList) {
                                resultList.add(new Triple(subject, predicate, object));
                    }}}
                } else {
                    // query logic for triples of type <?s ?p o>
                    // get all predicates for given object
                    List<BoundNode> predicateList = this.tripleFinderHelper.getAllPredicatesForObject((BoundNode) inputObject);
                    if ( predicateList == null || predicateList.isEmpty() ) { 
                        return null; }
                    // for each predicate for given object, get all subjects
                    for (BoundNode predicate: predicateList) {
                        List<BoundNode> subjectList = this.tripleFinderHelper.getAllSubjectsForPredicateAndObject(predicate, (BoundNode) inputObject);
                        if ( subjectList == null || subjectList.isEmpty() ) { continue; }
                        for (BoundNode subject: subjectList) {
                            resultList.add(new Triple(subject, predicate, (BoundNode) inputObject));
                    }}
                }
            } else {
                if ( inputObject.getNodeType() == NodeType.UNBOUND) {
                    // query logic for triples of type <?s p ?o>
                    // get all subjects for given predicate
                    List<BoundNode> subjectList = this.tripleFinderHelper.getAllSubjectsForPredicate((BoundNode) inputPredicate);
                    if ( subjectList == null || subjectList.isEmpty() ) { return null; }
                    // for each subject for given predicate, get all objects
                    for (BoundNode subject: subjectList) {
                        List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject((BoundNode) inputPredicate, subject);
                        if ( objectList == null || objectList.isEmpty() ) { continue; }
                        for (BoundNode object: objectList) {
                            resultList.add(new Triple(subject, (BoundNode) inputPredicate, object));
                    }}
                } else {
                    // query logic for triples of type <?s p o>
                    // get all subjects for given predicate and object
                    List<BoundNode> subjectList = this.tripleFinderHelper.getAllSubjectsForPredicateAndObject((BoundNode) inputPredicate, (BoundNode) inputObject);
                    if ( subjectList == null || subjectList.isEmpty() ) { return null; }
                    for (BoundNode subject: subjectList) {
                        resultList.add(new Triple(subject, (BoundNode) inputPredicate, (BoundNode) inputObject));
                    }
                }
            }
        } else {
            if ( inputPredicate.getNodeType() == NodeType.UNBOUND) {
                if ( inputObject.getNodeType() == NodeType.UNBOUND) {
                    // query logic for triples of type <s ?p ?o>
                    // get all predicates for given subject
                    List<BoundNode> predicateList = this.tripleFinderHelper.getAllPredicatesForSubject((BoundNode) inputSubject);
                    if ( predicateList == null || predicateList.isEmpty() ) { return null; }
                    // for each predicate for given subject, get all objects
                    for (BoundNode predicate: predicateList) {
                        List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject(predicate, (BoundNode) inputSubject);
                        if ( objectList == null || objectList.isEmpty() ) { continue; }
                        for (BoundNode object: objectList) {
                            resultList.add(new Triple((BoundNode) inputSubject, predicate, object));
                    }}
                } else {
                    // query logic for triples of type <s ?p o>
                    // get all predicates for given subject and object
                    List<BoundNode> predicateList = this.tripleFinderHelper.getAllPredicatesForSubjectAndObject((BoundNode) inputSubject, (BoundNode) inputObject);
                    if ( predicateList == null || predicateList.isEmpty() ) { return null; }
                    // for each predicate for given subject, get all objects
                    // check if givenobject is in list of objects and add triple to result list if it is
                    for (BoundNode predicate: predicateList) {
                        List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject(predicate, (BoundNode) inputSubject);
                        if ( objectList == null || objectList.isEmpty() ) { continue; }
                        if ( objectList.contains((BoundNode) inputObject) ) {
                            resultList.add(new Triple((BoundNode) inputSubject, predicate, (BoundNode) inputObject));
                    }}
                }
            } else {
                if ( inputObject.getNodeType() == NodeType.UNBOUND) {
                    // query logic for triples of type <s p ?o>
                    // get all objects for given predicate and subject
                    List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject((BoundNode) inputPredicate, (BoundNode) inputSubject);
                    if ( objectList == null || objectList.isEmpty() ) { return null; }
                    for (BoundNode object: objectList) {
                        resultList.add(new Triple((BoundNode) inputSubject, (BoundNode) inputPredicate, object));
                    }
                } else {
                    // query logic for triples of type <s p o>
                    // get all objects for given predicate and subject
                    // check if given object is in list of objects and add triple to result list if it is
                    List<BoundNode> objectList = this.tripleFinderHelper.getAllObjectsForPredicateAndSubject((BoundNode) inputPredicate, (BoundNode) inputSubject);
                    if ( objectList == null || objectList.isEmpty() ) { return null; }
                    if ( objectList.contains((BoundNode) inputObject) ) {
                        resultList.add(new Triple((BoundNode) inputSubject, (BoundNode) inputPredicate, (BoundNode) inputObject));
                }}
            }
        }
        // return null if no results were found
        if ( resultList == null || resultList.isEmpty() ) { return null; }
        // remove duplicates from result list and return
        return resultList.stream().distinct().collect(Collectors.toList());
    }
    
}