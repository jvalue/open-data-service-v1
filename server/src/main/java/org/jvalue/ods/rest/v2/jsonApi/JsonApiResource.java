package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonApi.JsonApiIdentifiable;
import org.jvalue.ods.api.jsonApi.JsonApiIdentifiableWithMetaData;
import org.jvalue.ods.api.jsonApi.MetaData;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResource extends JsonApiData {

    private String id;
    private final String type;
    private final JsonApiIdentifiable entity;
    private final MetaData meta;
    private Map<String, URI> links;

    private JsonApiResource(JsonApiIdentifiable entity, URI uri, MetaData meta) {
        super(uri);
        this.entity = entity;
        this.id = entity.getId();
        this.type = getUriRootElement(uri.toString()); //todo: use uriInfo or uri methods
        this.meta = meta;
    }

    public JsonApiResource(JsonApiIdentifiable entity,
                           URI uri) {
        this(entity, uri, null);
    }

    public JsonApiResource(JsonApiIdentifiableWithMetaData entity, URI uri) {
        this(entity, uri, entity.getMetaData());
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
    public MetaData getMeta() {
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
