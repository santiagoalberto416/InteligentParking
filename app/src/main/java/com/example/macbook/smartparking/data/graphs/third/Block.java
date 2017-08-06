
package com.example.macbook.smartparking.data.graphs.third;

import java.util.HashMap;
import java.util.Map;

public class Block {

    private Integer monthAmount;
    private Integer dayAmount;
    private Integer id;
    private String description;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(Integer monthAmount) {
        this.monthAmount = monthAmount;
    }

    public Integer getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(Integer dayAmount) {
        this.dayAmount = dayAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
