package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.*;

public class JsonApiDocument<T> {

    private Map<String, URI> links = new HashMap<>();
//    private Optional<JsonNode> meta = Optional.empty();
    private JsonApiData<T> data;
//    private Optional<JsonNode> errors = Optional.empty();
    private final UriInfo uriInfo;


    @SuppressWarnings("unchecked")
    public JsonApiDocument(T entity,
                           UriInfo uriInfo) {

        this.uriInfo = uriInfo;
        this.data = new JsonApiResource<T>(entity, uriInfo.getAbsolutePath());
        initLinks();
    }


    @SuppressWarnings("unchecked")
    public JsonApiDocument(Collection<T> entityCollection,
                           UriInfo uriInfo) {

        this.uriInfo = uriInfo;

        this.data = new JsonApiResourceArray<T>(entityCollection, uriInfo.getAbsolutePath());
        initLinks();
    }


    public Map<String, URI> getLinks() {
        return links;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    public Object getData() {
        return data;
    }


    /**
     * Initializes this documents links, i.e. adds a link to this documents endpoint.
     * If the document contains an array of resource objects, the method will add a selflink to each
     * contained resource object.
     */
    private void initLinks() {

        links.put("self", uriInfo.getAbsolutePath());

//        if(data instanceof JsonApiResourceArray) {
//           ((JsonApiResourceArray<T>) data).getResources()
//                   .forEach(r -> r.setSelfLink());
//
//        }
    }

}
