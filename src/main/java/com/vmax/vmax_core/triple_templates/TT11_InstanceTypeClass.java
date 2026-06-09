package com.vmax.vmax_core.triple_templates;

import java.util.List;

import com.vmax.vmax_core.api_elements.ApiClass;
import com.vmax.vmax_core.api_elements.ApiInstance;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #11 
 * </p>
 * <p>
 * <code>
 * [ApiInstance] rdf:type [ApiClass] .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT11_InstanceTypeClass extends TripleTemplate {

    public TT11_InstanceTypeClass(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDF_TYPE);
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.apiHelper.getAllInstances();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return ((ApiClass) object).getImplementingInstances();
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return ((ApiInstance) subject).getIndirectTypes();
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.getNodeType().equals(NodeType.API_CLASS);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_INSTANCE);
    }
    
}