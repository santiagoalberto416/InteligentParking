
package com.example.macbook.smartparking.data.sensorInfo;

import java.util.HashMap;
import java.util.Map;

public class Sensor {

    private String time;
    private Integer id;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
