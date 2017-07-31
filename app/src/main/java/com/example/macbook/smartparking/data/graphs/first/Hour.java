
package com.example.macbook.smartparking.data.graphs.first;

import java.util.HashMap;
import java.util.Map;

public class Hour {

    private Double amount;
    private String hour;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
