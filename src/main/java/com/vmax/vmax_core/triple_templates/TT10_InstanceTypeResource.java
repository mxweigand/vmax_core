package com.vmax.vmax_core.triple_templates;

import java.util.Collections;
import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #10 
 * </p>
 * <p>
 * <code>
 * [ApiInstance] rdf:type rdfs:Resource .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT10_InstanceTypeResource extends TripleTemplate {

    private final List<? extends BoundNode> objects;

    public TT10_InstanceTypeResource(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDF_TYPE);
        this.objects = Collections.singletonList(RdfsHelper.RDFS_RESOURCE);
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.apiHelper.getAllInstances();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return this.apiHelper.getAllInstances();
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return this.objects;
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.equals(RdfsHelper.RDFS_RESOURCE);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_INSTANCE);
    }

}