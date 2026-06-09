package com.vmax.vmax_core.triple_templates;

import java.util.List;

import com.vmax.vmax_core.api_elements.ApiClass;
import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #07
 * </p>
 * <p>
 * <code>
 * [ApiClass] rdfs:subClassOf [ApiClass(other class)] .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT07_ClassSubclassClass extends TripleTemplate {

    private final List<? extends BoundNode> allClasses;

    public TT07_ClassSubclassClass(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDFS_SUBCLASSOF);
        this.allClasses = this.apiHelper.getClassList();
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsSpec() {
        return this.allClasses;
    }

    @Override
    public List<? extends BoundNode> getAllSubjectsForObjectSpec(BoundNode object) {
        return ((ApiClass) object).getIndirectSubclasses();
    }

    @Override
    public List<? extends BoundNode> getAllObjectsForSubjectSpec(BoundNode subject) {
        return ((ApiClass) subject).getIndirectSuperclasses();
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