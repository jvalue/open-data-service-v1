package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonApiResource extends JsonApiResourceIdentifier implements JsonLinks {

    private final URI uri;
    private final JsonApiIdentifiable entity;
    private final Map<String, URI> links = new HashMap<>();

    public JsonApiResource(JsonApiIdentifiable entity, URI uri) {
        super(entity);
		this.uri = uri;
        this.entity = entity;
    }


    @JsonIgnoreProperties(value = {"id"})
    @JsonProperty("attributes")
    public JsonApiIdentifiable getEntity() {
        return entity;
    }


    @Override
    public Map<String, URI> getLinks() {
        return links;
    }


    @Override
    public void addLink(String name, URI ref) {
        links.put(name, ref);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JsonApiResource that = (JsonApiResource) o;
        return Objects.equals(uri, that.uri) &&
                Objects.equals(entity, that.entity);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uri, entity);
    }
}
