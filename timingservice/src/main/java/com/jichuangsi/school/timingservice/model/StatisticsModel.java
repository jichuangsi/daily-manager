package com.jichuangsi.school.timingservice.model;

public class StatisticsModel {
    private String header;//标题
    private String deptName;//部门
    private int peopleCount;//部门总人数
    private int shangRule;//上班规则
    private int xiaRule;//下班规则
    private int shangkao=0;//上班考勤人数（包含正常打卡，迟到，早退，考勤异常）
    private int xiakao=0;//下班考勤人数（包含正常打卡，迟到，早退，考勤异常）
    private int shangLostKao=0;//上班缺勤人数（统计当天没有打卡的人数）
    private int xiaLostKao=0;//下班缺勤人数（统计当天没有打卡的人数）
    private int yichang=0;//考勤异常人数

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getShangkao() {
        return shangkao;
    }

    public void setShangkao(int shangkao) {
        this.shangkao = shangkao;
    }

    public int getXiakao() {
        return xiakao;
    }

    public void setXiakao(int xiakao) {
        this.xiakao = xiakao;
    }

    public int getShangLostKao() {
        return shangLostKao;
    }

    public void setShangLostKao(int shangLostKao) {
        this.shangLostKao = shangLostKao;
    }

    public int getXiaLostKao() {
        return xiaLostKao;
    }

    public void setXiaLostKao(int xiaLostKao) {
        this.xiaLostKao = xiaLostKao;
    }

    public int getYichang() {
        return yichang;
    }

    public void setYichang(int yichang) {
        this.yichang = yichang;
    }

    public int getShangRule() {
        return shangRule;
    }

    public void setShangRule(int shangRule) {
        this.shangRule = shangRule;
    }

    public int getXiaRule() {
        return xiaRule;
    }

    public void setXiaRule(int xiaRule) {
        this.xiaRule = xiaRule;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
