package org.jvalue.ods.rest.v2.jsonapi.test;

public class JsonApiResource extends JsonApiResourceIdentifier {

    private String attributes = "attObj";

    public JsonApiResourceIdentifier toId() {
        return new JsonApiResourceIdentifier(this);
    }


}
