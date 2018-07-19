package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.utils.JsonUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonApiResource extends JsonApiResourceIdentifier implements JsonLinks {

    private final URI uri;
    private final JsonApiIdentifiable entity;
    private final Map<String, URI> links = new HashMap<>();
    private JsonApiRelationships relationships;

    public JsonApiResource(JsonApiIdentifiable entity, URI uri) {
        super(entity);
		this.uri = uri;
        this.entity = entity;
    }


    @JsonIgnoreProperties(value = {"id"})
    @JsonProperty("attributes")
    public Object getEntity() {
        return entity;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
	public JsonApiRelationships getRelationships() {
		return relationships;
	}


	@Override
    public Map<String, URI> getLinks() {
        return links;
    }


    @Override
    public void addLink(String name, URI ref) {
        links.put(name, ref);
    }


    public JsonApiResource addRelationship(String name, JsonApiIdentifiable entity, URI location) {
		if(relationships == null) {
			relationships = new JsonApiRelationships();
		}
		relationships.addRelationship(name, entity, location);
		return this;
	}


    public JsonApiResource restrictTo(String attribute) {
    	return new JsonApiResource(entity, uri) {
			@Override
			public Object getEntity() {
				return JsonUtils.createRestrictedEntity(entity, attribute);
			}
		};
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
