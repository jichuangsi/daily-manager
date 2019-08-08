package com.jichuangsi.school.timingservice.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "oplog")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class OpLog {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String operatorName;
    private String opActionname;
    private String opAction;
    private long createdTime = new Date().getTime();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOpActionname() {
        return opActionname;
    }

    public void setOpActionname(String opActionname) {
        this.opActionname = opActionname;
    }

    public String getOpAction() {
        return opAction;
    }

    public void setOpAction(String opAction) {
        this.opAction = opAction;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public OpLog() {
    }

    public OpLog(String operatorName, String opActionname, String opAction) {
        this.operatorName = operatorName;
        this.opActionname = opActionname;
        this.opAction = opAction;
    }
}
