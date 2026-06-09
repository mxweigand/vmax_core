package com.vmax.vmax_core.triple_templates;

import java.util.Collections;
import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #06
 * </p>
 * <p>
 * <code>
 * [ApiClass] rdfs:subClassOf [ApiClass(itself)] .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT06_ClassSubclassSelf extends TripleTemplate {

    private final List<? extends BoundNode> allClasses;

    public TT06_ClassSubclassSelf(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDFS_SUBCLASSOF);
        this.allClasses = this.apiHelper.getClassList();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.allClasses;
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return Collections.singletonList(object);
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return Collections.singletonList(subject);
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.getNodeType().equals(NodeType.API_CLASS);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_CLASS);
    }
    
}