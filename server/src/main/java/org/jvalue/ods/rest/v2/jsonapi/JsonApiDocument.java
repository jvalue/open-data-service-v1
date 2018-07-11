package org.jvalue.ods.rest.v2.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.*;

public abstract class JsonApiDocument {

    protected Map<String, URI> links = new HashMap<>();
    protected boolean isIdentifier;
    protected final UriInfo uriInfo;

    public JsonApiDocument(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        this.isIdentifier = false;
        initLinks();
    }

    public Map<String, URI> getLinks() {
        return links;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    public abstract Object getData();

    /**
     * if true, data is serialized using only type, id and selflink (JsonApiIdentifierObject)
     * @param isIdentifier
     */
    public void setIdentifier(boolean isIdentifier){
        this.isIdentifier = isIdentifier;
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
