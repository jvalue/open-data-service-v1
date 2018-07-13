package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;

public class JsonApiResource extends JsonApiResourceIdentifier {

    private final URI uri;
    private final JsonApiIdentifiable entity;

    public JsonApiResource(JsonApiIdentifiable entity, URI uri) {
        super(entity);
		this.uri = uri;
        this.entity = entity;
    }

    public JsonApiResourceIdentifier toIdentifier() {
        return new JsonApiResourceIdentifier(this);
    }

    @JsonIgnoreProperties(value = {"id"})
    @JsonProperty("attributes")
    public JsonApiIdentifiable getEntity() {
        return entity;
    }
}
