package com.vmax.vmax_core.triple_templates;

import java.util.List;
import java.util.stream.Collectors;

import com.vmax.vmax_core.api_elements.ApiAttribute;
import com.vmax.vmax_core.api_elements.ApiClass;
import com.vmax.vmax_core.api_elements.ApiEntity;
import com.vmax.vmax_core.api_elements.ApiInstance;
import com.vmax.vmax_core.api_elements.ApiType;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;

/**
 * <p> 
 * Class to represent triple template #12 
 * </p>
 * <p>
 * <code>
 * [ApiInstance] [ApiAttribute] [ApiEntity] .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated for each attribute of the API.
 * </p>
 */
public class TT12_InstanceAttributeEntity extends TripleTemplate {

    private final ApiAttribute apiAttribute;
    private final ApiClass attributeSourceClass;
    private final List<ApiClass> indirectAttributeSourceClasses;
    // private final ApiType attributeTargetType;
    private final List<ApiType> indirectAttributeTargetTypes;

    public TT12_InstanceAttributeEntity(ApiHelper apiHelper, ApiAttribute apiAttribute) {
        super(apiHelper,apiAttribute);
        this.apiAttribute = apiAttribute;
        this.attributeSourceClass = this.apiAttribute.getSourceClass();
        this.indirectAttributeSourceClasses = this.apiAttribute.getIndirectSourceClasses();
        // this.attributeTargetType = this.apiAttribute.getTargetType();
        this.indirectAttributeTargetTypes = this.apiAttribute.getIndirectTargetTypes();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        // get all instances of the attributes source class
        // using attributeSourceClass instead of indirectAttributeSourceClasses, 
        // becacause .getImplementingInstances() already gets all instances implementing th class or any of its subclasses
        return this.attributeSourceClass.getImplementingInstances();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        // first, get all instances of the attributes source class as in method above (getAllSubjectsSpec())
        List<ApiInstance> possibleSubjectInstances = this.attributeSourceClass.getImplementingInstances();
        if ( possibleSubjectInstances == null || possibleSubjectInstances.isEmpty() ) { return null; }
        // filter list for all instances returning the given object 
        // this is somehow like an "inverse property" in RDF, but we do not have this concept in our API
        List<ApiInstance> subjectInstances = possibleSubjectInstances.stream().filter(
            ( ApiInstance apiInstance ) -> {
                List<? extends BoundNode> targetEntities = this.apiAttribute.getTargetEntitiesForSourceInstance(apiInstance);
                if ( targetEntities == null || targetEntities.isEmpty() ) { return false; }
                return targetEntities.contains(object);
            }
        ).collect(Collectors.toList());
        if ( subjectInstances == null || subjectInstances.isEmpty() ) { return null; }
        return subjectInstances;
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return this.apiAttribute.getTargetEntitiesForSourceInstance((ApiInstance) subject); 
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        if ( !(object.getNodeType().equals(NodeType.API_INSTANCE) || object.getNodeType().equals(NodeType.API_VARIABLE)) ) { return false; }
        // get a list of all types of the object entity
        List<? extends ApiType> objectEntityTypes = ((ApiEntity) object).getIndirectTypes();
        // check if there is any intersection between this.indirectAttributeTargetTypes and objectEntityTypes
        boolean intersection = false;
        for (ApiType objectEntityType: objectEntityTypes) {
            if (this.indirectAttributeTargetTypes.contains(objectEntityType)) {
                intersection = true;
                break;
        }}
        return intersection;
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        if ( !subject.getNodeType().equals(NodeType.API_INSTANCE) ) { return false; }
        // get a list of all types (or classes) of the subject instance
        List<ApiClass> subjectInstanceClasses= ((ApiInstance) subject).getIndirectTypes();
        // check if there is any intersection between this.indirectAttributeSourceClasses and objectEntityTypes
        boolean intersection = false;
        for (ApiClass subjectInstanceClass: subjectInstanceClasses) {
            if (this.indirectAttributeSourceClasses.contains(subjectInstanceClass)) {
                intersection = true;
                break;
        }}
        return intersection;
    }

}