package com.vmax.vmax_core.triple_templates;

import java.util.Collections;
import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #05
 * </p>
 * <p>
 * <code>
 * [ApiClass] rdfs:subClassOf rdfs:Resource .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT05_ClassSubclassRessource extends TripleTemplate {

    private final List<? extends BoundNode> allClasses;
    private final List<? extends BoundNode> rdfsResourceAsList;

    public TT05_ClassSubclassRessource(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDFS_SUBCLASSOF);
        this.allClasses = this.apiHelper.getClassList();
        this.rdfsResourceAsList = Collections.singletonList(RdfsHelper.RDFS_RESOURCE);
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.allClasses;
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return this.allClasses;
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return this.rdfsResourceAsList;
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.equals(RdfsHelper.RDFS_RESOURCE);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_CLASS);
    }
    
}