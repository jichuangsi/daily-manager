package com.jichuangsi.school.timingservice.model;

public class UrlMapping {
    private String modelId;//模块id
    private String modelName;//模块名字
    private String staticPageId;//静态页面id
    private String staticPageName;//静态页面名字
    private String staticPageUrl;//静态页面URL
    private String roleUrlId;//可访问URLid/方法的id
    private String roleUrlName;//可访问URL名字/方法的名字
    private String roleUrl;//可访问URL/方法的URL

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

    public String getRoleUrlId() {
        return roleUrlId;
    }

    public void setRoleUrlId(String roleUrlId) {
        this.roleUrlId = roleUrlId;
    }

    public String getRoleUrlName() {
        return roleUrlName;
    }

    public void setRoleUrlName(String roleUrlName) {
        this.roleUrlName = roleUrlName;
    }

    public String getRoleUrl() {
        return roleUrl;
    }

    public void setRoleUrl(String roleUrl) {
        this.roleUrl = roleUrl;
    }
}
