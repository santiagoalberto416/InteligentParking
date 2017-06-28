
package com.example.macbook.smartparking.data.graphs.second;

import java.util.HashMap;
import java.util.Map;
public class SecondGraphResponse {

    private Analytics analytics;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Analytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Analytics analytics) {
        this.analytics = analytics;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
