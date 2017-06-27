
package com.example.macbook.smartparking.data.sensorInfo;

import java.util.HashMap;
import java.util.Map;

public class Hour {

    private Integer amount;
    private String hour;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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
