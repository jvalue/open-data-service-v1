package org.jvalue.ods.rest.v2.jsonapi.document;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

public class JsonApiResourceIdentifier {

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
        return this;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }


}
