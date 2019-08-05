package com.jichuangsi.school.timingservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "staticpage_url")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class StaticPageUrlRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private  String staticPageId;//静态页面ID
    private String urlId;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaticPageId() {
        return staticPageId;
    }

    public void setStaticPageId(String staticPageId) {
        this.staticPageId = staticPageId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }
}
