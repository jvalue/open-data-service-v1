package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.rest.v2.jsonapi.document.JsonApiDocument;

import java.util.Collection;
import java.util.stream.Collectors;

public class PluginMetaDataWrapper extends PluginMetaData implements JsonApiIdentifiable{

	private PluginMetaDataWrapper(String id, String author) {
		super(id, author);
	}


	@Override
	public String getType() {
		return PluginMetaData.class.getSimpleName();
	}


	public static PluginMetaDataWrapper from(PluginMetaData pluginMetaData) {
		return new PluginMetaDataWrapper(pluginMetaData.getId(), pluginMetaData.getAuthor());
	}


	public static Collection<PluginMetaDataWrapper> fromCollection(Collection<PluginMetaData> pluginMetaData) {
		return pluginMetaData.stream()
			.map(PluginMetaDataWrapper::from)
			.collect(Collectors.toList());
	}
}
