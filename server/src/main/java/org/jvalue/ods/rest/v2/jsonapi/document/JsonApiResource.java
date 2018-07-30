package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;
import org.jvalue.ods.utils.JsonUtils;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonApiResource extends JsonApiResourceIdentifier implements JsonLinks {

    private final URI uri;
    private final JsonApiIdentifiable entity;
    private final Map<String, URI> links = new HashMap<>();
    private final Map<String, JsonApiRelationship> relationships = new HashMap<>();

    public JsonApiResource(JsonApiIdentifiable entity, URI uri) {
        super(entity);
		this.uri = uri;
        this.entity = entity;
    }


    @JsonIgnoreProperties(value = {"id", "type"})
    @JsonProperty("attributes")
    public Object getEntity() {
        return entity;
    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Map<String, JsonApiRelationship> getRelationships() {
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


	@Override
	public URI getURI() {
		return uri;
	}


	public boolean hasRelationshipTo(JsonApiIdentifiable related) {
		return relationships.entrySet().stream()
			.anyMatch(r -> r.getValue().containsEntity(related));
	}


	public URI getRelationshipUri(JsonApiIdentifiable related) {
		URI relationshipURI = null;

		for (Map.Entry<String, JsonApiRelationship> entry: relationships.entrySet()) {
			JsonApiRelationship match = entry.getValue();
			if(match.containsEntity(related)) {
				if(match.getData().size() == 1) { //the relationship contains a single resource object
					relationshipURI =  match.getLinks().get(RELATED);
				} else { //the relationship contains a collection of resource objects
					relationshipURI = match.getLinks().get(RELATED).resolve(related.getId());
				}
				break;
			}
		}

		return relationshipURI;
	}


    public JsonApiResource addRelationship(String name, JsonApiIdentifiable related, URI location) {
		relationships.put(name, new JsonApiRelationship(related, location));
		return this;
	}


	public JsonApiResource addRelationship(String name, Collection<? extends JsonApiIdentifiable> relatedCollection, URI location) {
		relationships.put(name, new JsonApiRelationship(relatedCollection, location));
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
