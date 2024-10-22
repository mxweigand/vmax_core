package com.vmax.vmax_core.triple_templates;

import java.util.List;

import com.vmax.vmax_core.api_elements.ApiAttribute;
import com.vmax.vmax_core.api_elements.ApiType;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #09 
 * </p>
 * <p>
 * <code>
 * [ApiAttribute] rdfs:range [ApiType] .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT09_AttributeRangeType extends TripleTemplate {

    private final List<? extends BoundNode> allAttributes;

    public TT09_AttributeRangeType(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDFS_RANGE);
        this.allAttributes = this.apiHelper.getAttributeList();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.allAttributes;
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return ((ApiType) object).getTargetAttributes();
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return ((ApiAttribute) subject).getIndirectTargetTypes();
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.getNodeType().equals(NodeType.API_CLASS) || object.getNodeType().equals(NodeType.API_DATATYPE);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_ATTRIBUTE);
    }
    
}