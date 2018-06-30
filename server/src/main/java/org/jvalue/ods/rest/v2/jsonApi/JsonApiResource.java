package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Optional;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;
import static org.jvalue.ods.utils.JsonUtils.getPropertyValueNode;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResource<T> {

    private final String selfReference;
    private final String id;
    private final String type;
    private final T entity;
    private final Optional<JsonNode> meta;
    private Optional<HashMap<String, String>> links = Optional.empty();

    public JsonApiResource(T entity,
                           String uri,
                           String id) {

        this.entity = entity;
        this.selfReference = uri;
        this.type = getUriRootElement(uri);
        this.id = id;
        this.meta = Optional.ofNullable(getPropertyValueNode(entity, "metaData"));
    }


    public JsonApiResource(T entity,
                           String uri) {
        this(entity, uri, getIdFromObject(entity));
    }


    public void setSelfLink(){
        setLink("self", selfReference);
    }


    public void setLink(String name,
                        String ref) {
        if(!links.isPresent()) {
            links = Optional.of(new HashMap<String, String>());
        }
        links.get().put(name, ref);
    }


    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<JsonNode> getMeta() {
        return meta;
    }


    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<HashMap<String, String>> getLinks() {
        return links;
    }


    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }


    @JsonProperty("attributes")
    @JsonIgnoreProperties(value = {"id", "metaData"})
    public T getEntity() {
        return entity;
    }
}
