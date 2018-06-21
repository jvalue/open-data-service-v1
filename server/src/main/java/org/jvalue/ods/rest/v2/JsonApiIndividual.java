package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jvalue.ods.utils.JsonUtils.getPropertyValueNode;
import static org.jvalue.ods.utils.JsonUtils.getPropertyValueString;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiIndividual<T> extends JsonApiObject<T> {

    private final Optional<JsonNode> meta;
    private final Data<T> data;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<JsonNode> getMeta() {
        return meta;
    }

    public Data<T> getData() {
        return data;
    }


    public JsonApiIndividual(T payload, String selfReference) {
        super(selfReference);
        this.data = new Data<>(payload, getUriRootElement(selfReference));
        this.meta = Optional.ofNullable(getPropertyValueNode(payload, "metaData"));
    }

    public JsonApiIndividual(T payload, String selfReference, String type, String id) {
        super(selfReference);
        this.data = new Data<>(payload, type, id);
        this.meta = Optional.ofNullable(getPropertyValueNode(payload, "metaData"));
    }

    public JsonApiIndividual(T payload, String selfReference, Map<String, String> linkMap) {
        this(payload, selfReference);
        links.addLinks(linkMap);
    }

    private class Data<T> {
        private final String type;
        private final String id;

        @JsonIgnoreProperties(value = {"id", "metaData"})
        private final T attributes;

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public T getAttributes() {
            return attributes;
        }

        public Data(T attributes, String type) {
            this.attributes = attributes;
            this.type = type;
            this.id = getPropertyValueString(attributes, "id");
        }

        public Data(T attributes, String type, String id) {
            this.attributes = attributes;
            this.type = type;
            this.id = id;
        }
    }
}
