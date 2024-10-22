package com.vmax.vmax_core.api_helper;

import java.util.List;

import com.vmax.vmax_core.api_elements.ApiAttribute;
import com.vmax.vmax_core.api_elements.ApiClass;

public abstract class ApiClassAndAttributeList {
    
    //
    protected List<ApiAttribute> attributeList;
    protected List<ApiClass> classList;
    //
    protected final String attributeUriPrefix;
    protected final String classUriPrefix;
    protected final String instanceUriPrefix;

    public ApiClassAndAttributeList(String classUriPrefix, String attributeUriPrefix, String instanceUriPrefix) {
        this.classUriPrefix = classUriPrefix;
        this.attributeUriPrefix = attributeUriPrefix;
        this.instanceUriPrefix = instanceUriPrefix;
    }

    public List<ApiAttribute> getAttributeList() {
        return attributeList;
    }

    public List<ApiClass> getClassList() {
        return classList;
    }

}
