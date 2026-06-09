package com.vmax.vmax_core.triple_templates;

import java.util.Collections;
import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #03
 * </p>
 * <p>
 * <code>
 * [ApiAttribute] rdf:type rdf:Property .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT03_AttributeTypeProperty extends TripleTemplate {

    private final List<? extends BoundNode> allAttributes;
    private final List<? extends BoundNode> rdfPropertyAsList;

    public TT03_AttributeTypeProperty(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDF_TYPE);
        this.allAttributes = this.apiHelper.getAttributeList();
        this.rdfPropertyAsList = Collections.singletonList(RdfsHelper.RDF_PROPERTY);
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.allAttributes;
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return this.allAttributes;
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return this.rdfPropertyAsList;
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.equals(RdfsHelper.RDF_PROPERTY);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_ATTRIBUTE);
    }
    
}