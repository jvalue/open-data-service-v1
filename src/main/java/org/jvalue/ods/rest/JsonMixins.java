package org.jvalue.ods.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataView;
import org.jvalue.ods.filter.reference.FilterChainReference;

import java.util.HashMap;
import java.util.Map;


public class JsonMixins {

	@JsonIgnoreProperties({"_id", "_rev"})
	private static interface DataSourceMixin {
		@JsonProperty("id") String getSourceId();
	}

	@JsonIgnoreProperties({"_id", "_rev"})
	private static interface FilterChainReferenceMixin {
		@JsonProperty("id") String getFilterChainId();
	}

	@JsonIgnoreProperties({"_id", "_rev"})
	private static interface DataViewMixin {
		@JsonProperty("id") String getViewId();
	}


	private final Map<Class<?>, Class<?>> mixins = new HashMap<>();

	public JsonMixins() {
		mixins.put(DataSource.class, DataSourceMixin.class);
		mixins.put(FilterChainReference.class, FilterChainReferenceMixin.class);
		mixins.put(DataView.class, DataViewMixin.class);
	}


	public Map<Class<?>, Class<?>> getMixins() {
		return mixins;
	}

}
