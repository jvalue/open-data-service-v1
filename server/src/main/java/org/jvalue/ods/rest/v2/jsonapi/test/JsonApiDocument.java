package org.jvalue.ods.rest.v2.jsonapi.test;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonApiDocument implements Serializable {

    protected Map<String, String> links = new HashMap<>();

    @JsonFormat(with = {JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY})
    protected List<JsonApiResourceIdentifier> data = new LinkedList<>();

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public List<JsonApiResourceIdentifier> getData() {
        return data;
    }

    public void setData(List<JsonApiResourceIdentifier> data) {
        this.data = data;
    }
}
