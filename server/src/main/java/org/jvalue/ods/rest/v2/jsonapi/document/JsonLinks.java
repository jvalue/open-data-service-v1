package org.jvalue.ods.rest.v2.jsonapi.document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;

public interface JsonLinks {

    String SELF = "self";

    Map<String, URI> getLinks();


    void addLink(String name, URI ref);


    @JsonIgnore
    default URI getSelfLink() {
        return getLinks().get(SELF);
    }


    default void addSelfLink(UriInfo uriInfo) {
        addLink(SELF, uriInfo.getAbsolutePath());
    }


    default void addSelfLink(URI uri) {
        addLink(SELF, uri);
    }

}
