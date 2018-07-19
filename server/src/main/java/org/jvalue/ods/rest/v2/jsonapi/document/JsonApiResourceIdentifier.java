package org.jvalue.ods.rest.v2.jsonapi.document;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import java.util.Objects;

public class JsonApiResourceIdentifier implements JsonApiIdentifiable {

    private final String id;
    private final String type;


    public JsonApiResourceIdentifier(JsonApiResourceIdentifier identifier) {
        this.id = identifier.id;
        this.type = identifier.type;
    }


    public JsonApiResourceIdentifier(JsonApiIdentifiable entity) {
        this.id = entity.getId();
        this.type = entity.getClass().getSimpleName();
    }


    public JsonApiResourceIdentifier toIdentifier() {
    	return new JsonApiResourceIdentifier(this);
	}


    @Override
    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonApiResourceIdentifier that = (JsonApiResourceIdentifier) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
