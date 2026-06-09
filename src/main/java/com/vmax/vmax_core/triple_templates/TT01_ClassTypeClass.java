package com.vmax.vmax_core.triple_templates;

import java.util.Collections;
import java.util.List;

import com.vmax.vmax_core.api_helper.ApiHelper;
import com.vmax.vmax_core.graph.BoundNode;
import com.vmax.vmax_core.graph.NodeType;
import com.vmax.vmax_core.graph.RdfsHelper;

/**
 * <p>
 * Class to represent triple template #01 
 * </p>
 * <p>
 * <code>
 * [ApiClass] rdf:type rdfs:Class .
 * </code>
 * </p>
 * <p>
 * This triple template will be instantiated only once.
 * </p>
 */
public class TT01_ClassTypeClass extends TripleTemplate {

    private final List<? extends BoundNode> allClasses;
    private final List<? extends BoundNode> rdfsClassAsList;

    public TT01_ClassTypeClass(ApiHelper apiHelper) {
        super(apiHelper, RdfsHelper.RDF_TYPE);
        this.allClasses = this.apiHelper.getClassList();
        this.rdfsClassAsList = Collections.singletonList(RdfsHelper.RDFS_CLASS);
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
        return this.rdfsClassAsList;
    }

    @Override
    public boolean isApplicableForObjectSpec(BoundNode object) {
        return object.equals(RdfsHelper.RDFS_CLASS);
    }

    @Override
    public boolean isApplicableForSubjectSpec(BoundNode subject) {
        return subject.getNodeType().equals(NodeType.API_CLASS);
    }
    
}