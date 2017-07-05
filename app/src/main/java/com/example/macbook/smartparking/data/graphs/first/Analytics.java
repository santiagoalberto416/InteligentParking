
package com.example.macbook.smartparking.data.graphs.first;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analytics {

    private String month;
    private List<Hour> hours = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
