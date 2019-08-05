package com.jichuangsi.school.timingservice.model;

public class UrlMapping {
    private String modelId;//模块id
    private String modelName;//模块名字
    private String staticPageId;//静态页面id
    private String staticPageName;//静态页面名字
    private String staticPageUrl;//静态页面URL
    private String id;//可访问URLid/方法的id
    private String name;//可访问URL名字/方法的名字
    private String roleUrl;//可访问URL/方法的URL
    private String relationId;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getStaticPageId() {
        return staticPageId;
    }

    public void setStaticPageId(String staticPageId) {
        this.staticPageId = staticPageId;
    }

    public String getStaticPageName() {
        return staticPageName;
    }

    public void setStaticPageName(String staticPageName) {
        this.staticPageName = staticPageName;
    }

    public String getStaticPageUrl() {
        return staticPageUrl;
    }

    public void setStaticPageUrl(String staticPageUrl) {
        this.staticPageUrl = staticPageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleUrl() {
        return roleUrl;
    }

    public void setRoleUrl(String roleUrl) {
        this.roleUrl = roleUrl;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
