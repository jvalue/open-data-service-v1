package org.jvalue.ods.rest.v2;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.HashMap;
import java.util.Map;

public abstract class JsonApiObject<T> {
    protected final String selfReference;
    protected final Links links;

    public JsonApiObject(String selfReference) {
        this.selfReference = selfReference;
        this.links = new Links(selfReference);
    }

    public Links getLinks() {
        return links;
    }

    protected class Links {
        private final Map<String, String> links = new HashMap<>();

        @JsonAnyGetter
        public Map<String, String> getLinks() {
            return links;
        }

        public Links(String selfReference) {
            links.put("self", selfReference);
        }

        void addLinks(Map<String, String> links) {
            links.putAll(links);
        }
    }

}
