package org.jvalue.ods.rest.v2.jsonApi;

import java.net.URI;

public abstract class JsonApiData<T>  {
    protected final URI uri;

    public JsonApiData(URI uri) {
        this.uri = uri;
    }
}
