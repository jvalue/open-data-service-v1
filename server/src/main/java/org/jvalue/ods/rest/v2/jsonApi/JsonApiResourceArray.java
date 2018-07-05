package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class JsonApiResourceArray<T> extends JsonApiData {

    private final Collection<JsonApiResource<T>> jsonApiResources;


    public JsonApiResourceArray(Collection<T> resources, URI uri) {
        super(uri);
        this.jsonApiResources = new ArrayList<>();
        resources.forEach(r -> jsonApiResources
                .add(
                        new JsonApiResource<T>(r, uri.resolve(getIdFromObject(r)))));
    }

    public Collection<JsonApiResource<T>> getResources() {
        return jsonApiResources;
    }

    private void addAsJsonApiResource(T resource, Collection<T> collection) {

    }
    //nothing more to do here for now. Later on pagination methods may be implemented here.
}
