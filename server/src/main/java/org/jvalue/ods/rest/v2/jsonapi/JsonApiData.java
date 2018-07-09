package org.jvalue.ods.rest.v2.jsonapi;

import java.net.URI;

public abstract class JsonApiData  {
    protected final URI uri;

    public JsonApiData(URI uri) {
        this.uri = uri;
    }
}
