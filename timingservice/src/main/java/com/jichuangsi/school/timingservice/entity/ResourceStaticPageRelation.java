package com.jichuangsi.school.timingservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "resource_staticpage")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class ResourceStaticPageRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String resourceId;//模块id
    private  String staticPageId;//静态页面ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getStaticPageId() {
        return staticPageId;
    }

    public void setStaticPageId(String staticPageId) {
        this.staticPageId = staticPageId;
    }
}
