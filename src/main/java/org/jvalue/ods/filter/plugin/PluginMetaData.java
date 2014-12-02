package org.jvalue.ods.filter.plugin;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;

public final class PluginMetaData extends CouchDbDocument {

	private final String pluginId;

	@JsonCreator
	public PluginMetaData(
			@JsonProperty("pluginId") String pluginId) {

		this.pluginId = pluginId;
	}


	public String getPluginId() {
		return pluginId;
	}

}
