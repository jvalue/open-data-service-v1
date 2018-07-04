package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;
import static org.jvalue.ods.utils.JsonUtils.getPropertyValueNode;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResource<T> {

    private final UriInfo uriInfo;
    private final String id;
    private final String type;
    private final T entity;
    private final Optional<JsonNode> meta;
    private Map<String, URI> links;

    public JsonApiResource(T entity,
                           UriInfo uriInfo,
                           String id) {

        this.entity = entity;
        this.uriInfo = uriInfo;
        this.type = uriInfo.getBaseUri().getQuery();
        this.id = id;
        this.meta = Optional.ofNullable(getPropertyValueNode(entity, "metaData"));
    }


    public JsonApiResource(T entity,
                           UriInfo uriInfo) {
        this(entity, uriInfo, getIdFromObject(entity));
    }


    public void setSelfLink(){
        setLink("self", uriInfo.getAbsolutePath());
    }


    public void setLink(String name,
                        URI ref) {
        if(links == null) {
            links = new HashMap<>();
        }
        links.put(name, ref);
    }


    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<JsonNode> getMeta() {
        return meta;
    }


    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Map<String, URI> getLinks() {
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
