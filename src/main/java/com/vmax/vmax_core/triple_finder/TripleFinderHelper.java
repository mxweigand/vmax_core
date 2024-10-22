package com.vmax.vmax_core.triple_finder;

import java.util.ArrayList;
import java.util.List;

import com.vmax.vmax_core.api_elements.ApiAttribute;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.triple_templates.TT01_ClassTypeClass;
import com.vmax.vmax_core.triple_templates.TT02_ClassTypeResource;
import com.vmax.vmax_core.triple_templates.TT03_AttributeTypeProperty;
import com.vmax.vmax_core.triple_templates.TT04_AttributeTypeResource;
import com.vmax.vmax_core.triple_templates.TT05_ClassSubclassRessource;
import com.vmax.vmax_core.triple_templates.TT06_ClassSubclassSelf;
import com.vmax.vmax_core.triple_templates.TT07_ClassSubclassClass;
import com.vmax.vmax_core.triple_templates.TT08_AttributeDomainClass;
import com.vmax.vmax_core.triple_templates.TT09_AttributeRangeType;
import com.vmax.vmax_core.triple_templates.TT10_InstanceTypeResource;
import com.vmax.vmax_core.triple_templates.TT11_InstanceTypeClass;
import com.vmax.vmax_core.triple_templates.TT12_InstanceAttributeEntity;
import com.vmax.vmax_core.triple_templates.TripleTemplate;

/**
 * <p>
 * This class is a collection of helper methods for {@link com.vmax.vmax_core.triple_finder.TripleFinder}.
 * </p>
 * <p>
 * It is instantiated by the {@link com.vmax.vmax_core.triple_finder.TripleFinder <code>TripleFinder</code>} 
 * by passing an isntance of {@link com.vmax.vmax_core.api_helper.ApiHelper <code>ApiHelper</code>}.
 * </p>
 * <p>
 * The core of this class is the list of {@link com.vmax.vmax_core.triple_templates.TripleTemplate <code>TripleTemplates</code>}
 * which is initialized in the constructor.
 * </p>
 */
public class TripleFinderHelper {

    private List<TripleTemplate> tripleTemplateList;

    public TripleFinderHelper(ApiHelper apiHelper) {
        // initialize list for all triple templates 
        this.tripleTemplateList = new ArrayList<TripleTemplate>();
        // create the first 11 (static) triple templates
        this.tripleTemplateList.add(new TT01_ClassTypeClass(apiHelper));
        this.tripleTemplateList.add(new TT02_ClassTypeResource(apiHelper));
        this.tripleTemplateList.add(new TT03_AttributeTypeProperty(apiHelper));
        this.tripleTemplateList.add(new TT04_AttributeTypeResource(apiHelper));
        this.tripleTemplateList.add(new TT05_ClassSubclassRessource(apiHelper));
        this.tripleTemplateList.add(new TT06_ClassSubclassSelf(apiHelper));
        this.tripleTemplateList.add(new TT07_ClassSubclassClass(apiHelper));
        this.tripleTemplateList.add(new TT08_AttributeDomainClass(apiHelper));
        this.tripleTemplateList.add(new TT09_AttributeRangeType(apiHelper));
        this.tripleTemplateList.add(new TT10_InstanceTypeResource(apiHelper));
        this.tripleTemplateList.add(new TT11_InstanceTypeClass(apiHelper));
        // get list of all attributes
        List<ApiAttribute> allApiAttributes = apiHelper.getAttributeList();
        // create one triple template (type TT12) for each
        for (ApiAttribute  apiAttribute: allApiAttributes) {
            this.tripleTemplateList.add(new TT12_InstanceAttributeEntity(apiHelper, apiAttribute));
        }
    }

    /**
     * Returns a list of all predicates by collecting the predicates of all triple templates.
     */
    public List<BoundNode> getAllPredicates() {
        List<BoundNode> predicates = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            predicates.add(tripleTemplate.getPredicate()); 
        }
        return predicates;
    }

    /**
     * Returns a list of all predicates based on a given subject.
     * Each triple template is checked if it is applicable for the given subject.
     * The predicates of all applicable triple templates are collected.
     * @param subject - The subject to filter the predicates
     */
    public List<BoundNode> getAllPredicatesForSubject(BoundNode subject) {
        // initialize empty list of predicates
        List<BoundNode> predicates = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate if triple template is applicable for the given subject
            if (tripleTemplate.isApplicableForSubject(subject)) {
                predicates.add(tripleTemplate.getPredicate()); 
            //else, skip triple template
            } else { continue; }
        }
        if (predicates.isEmpty()) { return null; }
        return predicates;
    }

    /**
     * Returns a list of all predicates based on a given object.
     * Each triple template is checked if it is applicable for the given object.
     * The predicates of all applicable triple templates are collected.
     * @param object - The object to filter the predicates
     */
    public List<BoundNode> getAllPredicatesForObject(BoundNode object) {
        // initialize empty list of predicates
        List<BoundNode> predicates = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate if triple template is applicable for the given object
            if (tripleTemplate.isApplicableForObject(object)) {
                predicates.add(tripleTemplate.getPredicate()); 
            //else, skip triple template
            } else { continue; }
        }
        if (predicates.isEmpty()) { return null; }
        return predicates;
    }

    /**
     * Returns a list of all predicates based on a given subject and object.
     * Each triple template is checked if it is applicable for the given subject and object.
     * The predicates of all applicable triple templates are collected.
     * @param subject - The subject to filter the predicates
     * @param object - The object to filter the predicates
     */
    public List<BoundNode> getAllPredicatesForSubjectAndObject(BoundNode subject, BoundNode object) {
        // initialize empty list of predicates
        List<BoundNode> predicates = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate if triple template is applicable for the given subject and object
            if (tripleTemplate.isApplicableForSubjectAndObject(subject, object)) {
                predicates.add(tripleTemplate.getPredicate()); 
            //else, skip triple template
            } else { continue; }
        }
        if (predicates.isEmpty()) { return null; }
        return predicates;
    }
        
    /**
     * Returns a list of all subjects based on a given predicate.
     * Each triple template is checked if it uses the given predicate.
     * If so, the subjects of that triple template are collected.
     * @param predicate - The predicate to filter the subjects
     */
    public List<BoundNode> getAllSubjectsForPredicate(BoundNode predicate) {
        // initialize empty list of subjects
        List<BoundNode> subjects = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate triple template if it uses the given predicate
            if (tripleTemplate.getPredicate().equals(predicate)) { 
                List<? extends BoundNode> tripleTemplateSubjects = tripleTemplate.getAllSubjects();
                if (tripleTemplateSubjects != null && !tripleTemplateSubjects.isEmpty()) 
                    { subjects.addAll(tripleTemplateSubjects); }
            //else, skip triple template
            } else { continue; }
        }
        if (subjects.isEmpty()) { return null; }
        return subjects;
    }

    /**
     * Returns a list of all subjects based on a given predicate and object.
     * Each triple template is checked if it uses the given predicate.
     * If so, all subjects of the triple template matching the given object are collected.
     * @param predicate - The predicate to filter the subjects
     * @param object - The object to filter the subjects
     */
    public List<BoundNode> getAllSubjectsForPredicateAndObject(BoundNode predicate, BoundNode object) {
        // initialize empty list of subjects
        List<BoundNode> subjects = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate triple template if it uses the given predicate
            if (tripleTemplate.getPredicate().equals(predicate)) {
                List<? extends BoundNode> tripleTemplateSubjects = tripleTemplate.getAllSubjectsForObject(object);
                if (tripleTemplateSubjects != null && !tripleTemplateSubjects.isEmpty()) 
                    { subjects.addAll(tripleTemplateSubjects); } 
            //else, skip triple template
            } else { continue; }
        }
        if (subjects.isEmpty()) { return null; }
        return subjects;
    }

    /**
     * Returns a list of all objects by based on a given subject.
     * Each triple template is checked if it is applicable for the given subject.
     * If so, all objects of the triple template matching the given subject are collected.
     * @param predicate - The predicate to filter the objects
     * @param subject - The subject to filter the objects
     */
    public List<BoundNode> getAllObjectsForPredicateAndSubject(BoundNode predicate, BoundNode subject) {
        // initialize empty list of objects
        List<BoundNode> objects = new ArrayList<BoundNode>();
        for (TripleTemplate tripleTemplate: tripleTemplateList) {
            // evaluate triple template if it uses the given predicate
            if (tripleTemplate.getPredicate().equals(predicate)) { 
                List<? extends BoundNode> tripleTemplateObjects = tripleTemplate.getAllObjectsForSubject(subject);
                if (tripleTemplateObjects != null && !tripleTemplateObjects.isEmpty()) 
                    { objects.addAll(tripleTemplateObjects); }
                //else, skip triple template
            } else { continue; }
        }
        if (objects.isEmpty()) { return null; }
        return objects;
    }

}
