package org.jvalue.ods.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.filter.reference.FilterChainReference;

import java.util.HashMap;
import java.util.Map;


public class JsonMixins {

	@JsonIgnoreProperties({"_id", "_rev"})
	private static interface CouchDbMixin { }

	@JsonIgnoreProperties({"_id", "_rev", "dataSourceId"})
	private static interface FilterChainReferenceMixin { }


	private final Map<Class<?>, Class<?>> mixins = new HashMap<>();

	public JsonMixins() {
		mixins.put(CouchDbDocument.class, CouchDbMixin.class);
		mixins.put(FilterChainReference.class, FilterChainReferenceMixin.class);
	}


	public Map<Class<?>, Class<?>> getMixins() {
		return mixins;
	}

}
