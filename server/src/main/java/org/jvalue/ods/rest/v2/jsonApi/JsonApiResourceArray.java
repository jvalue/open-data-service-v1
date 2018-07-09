package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jvalue.ods.api.jsonApi.JsonApiIdentifiable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;


@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class JsonApiResourceArray extends JsonApiData {

    private final Collection<JsonApiData> jsonApiResources;


    public JsonApiResourceArray(Collection<? extends JsonApiIdentifiable> resources, URI uri) {
        super(uri);
        this.jsonApiResources = new ArrayList<>();
        resources.forEach(r -> jsonApiResources
                .add(
                        new JsonApiResourceIdentifier(r,
                                uri.resolve(r.getId()))
                                .initLinks()));
    }

    public Collection<JsonApiData> getResources() {
        return jsonApiResources;
    }

    //nothing more to do here for now. Later on pagination methods may be implemented here.
}
