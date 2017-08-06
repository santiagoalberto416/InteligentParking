
package com.example.macbook.smartparking.data.graphs.third;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockResponse {

    private List<Block> blocks = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
