package org.jvalue.ods.rest.v2.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.*;

public class JsonApiDocument {

    private Map<String, URI> links = new HashMap<>();
//    private Optional<JsonNode> meta = Optional.empty();
    private JsonApiData data;
//    private Optional<JsonNode> errors = Optional.empty();
    private final UriInfo uriInfo;

    public JsonApiDocument(JsonApiIdentifiable entity, UriInfo uriInfo, boolean isIdentifier) {
        this.uriInfo = uriInfo;
        if(isIdentifier) {
            this.data = new JsonApiResourceIdentifier(entity, uriInfo.getAbsolutePath());
        } else {
            this.data = new JsonApiResource(entity, uriInfo.getAbsolutePath());
        }
        initLinks();
    }

    public JsonApiDocument(JsonApiIdentifiable entity,
                           UriInfo uriInfo) {

        this(entity, uriInfo, false);
    }


    public JsonApiDocument(Collection<? extends JsonApiIdentifiable> entityCollection,
                           UriInfo uriInfo) {

        this.uriInfo = uriInfo;

        this.data = new JsonApiResourceArray(entityCollection, uriInfo.getAbsolutePath());
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
