
package com.example.macbook.smartparking.data.sensorInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdminResponse {

    private List<Sensor> sensors = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
