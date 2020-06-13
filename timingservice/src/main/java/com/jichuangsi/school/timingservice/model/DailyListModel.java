package com.jichuangsi.school.timingservice.model;

import java.util.List;
import java.util.Map;

public class DailyListModel {
    private List<ReportFormModel2> model2s;
    private Map<String,Object> statistics;

    public List<ReportFormModel2> getModel2s() {
        return model2s;
    }

    public void setModel2s(List<ReportFormModel2> model2s) {
        this.model2s = model2s;
    }

    public Map<String, Object> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Object> statistics) {
        this.statistics = statistics;
    }
}
