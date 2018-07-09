package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.jsonApi.JsonApiIdentifiable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;
import static org.jvalue.ods.utils.JsonUtils.getPropertyValueNode;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResource extends JsonApiData {

    private String id;
    private final String type;
    private final JsonApiIdentifiable entity;
    private final JsonNode meta;
    private Map<String, URI> links;


    public JsonApiResource(JsonApiIdentifiable entity,
                           URI uri) {
        super(uri);
        this.entity = entity;
        String id;
        try {
            id = getIdFromObject(entity);
        } catch (IllegalArgumentException e) {
            id = "NO_ID"; //todo: entity has no id property. what to do now?
        }
        this.id = id;
        this.type = getUriRootElement(uri.toString()); //todo: use uriInfo or uri methods
        this.meta = getPropertyValueNode(entity, "metaData");
    }


    public void setSelfLink(){
        setLink("self", uri);
    }


    public void setLink(String name,
                        URI ref) {
        if(links == null) {
            links = new HashMap<>();
        }
        links.put(name, ref);
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public JsonNode getMeta() {
        return meta;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    public JsonApiIdentifiable getEntity() {
        return entity;
    }
}
