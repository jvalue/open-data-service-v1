/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Map;

public interface JsonLinks {

    String SELF = "self";
	String RELATED = "related";


	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, URI> getLinks();


    void addLink(String name, URI ref);


    @JsonIgnore
    URI getURI();


    @JsonIgnore
    default URI getSelfLink() {
        return getLinks().get(SELF);
    }


    default void addSelfLink() {
        addLink(SELF, getURI());
    }

}
