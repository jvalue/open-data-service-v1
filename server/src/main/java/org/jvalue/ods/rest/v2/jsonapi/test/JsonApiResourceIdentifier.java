package org.jvalue.ods.rest.v2.jsonapi.test;

public class JsonApiResourceIdentifier {

    public JsonApiResourceIdentifier(JsonApiResourceIdentifier identifier) {
        this.id = identifier.id;
        this.type = identifier.type;
    }

    public JsonApiResourceIdentifier () {}

    private String id = "42";

    private String type = "user";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
