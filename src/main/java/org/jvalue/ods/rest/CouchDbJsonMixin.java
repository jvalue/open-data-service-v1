package org.jvalue.ods.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({"_id", "_rev"})
public final class CouchDbJsonMixin { }
