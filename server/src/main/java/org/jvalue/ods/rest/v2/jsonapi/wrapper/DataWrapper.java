package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jvalue.ods.utils.JsonUtils.getMapFromJson;

public class DataWrapper implements JsonApiIdentifiable{

	private final String id;
	private Map<String, Object> attributes;

	private DataWrapper(JsonNode jsonNode, String domainIdKey) {
		this.id = getDomainId(jsonNode, domainIdKey);
		try {
			this.attributes = getMapFromJson(jsonNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String getId() {
		return id;
	}


	@Override
	public String getType() {
		return Data.class.getSimpleName();
	}


	@JsonAnyGetter
	@JsonIgnoreProperties({"_id","_rev"})
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	public static DataWrapper from(JsonNode node, DataSource source) {
		return new DataWrapper(node, getDomainIdKey(source));
	}


	public static Collection<DataWrapper> fromCollection(Collection<JsonNode> nodes, DataSource source) {
		return nodes.stream()
			.map(n -> DataWrapper.from(n, source))
			.collect(Collectors.toList());
	}


	private static String getDomainId(JsonNode node, String domainIdKey) {
		if(!node.has(domainIdKey)) {
			//log error
			return "NO_ID";
		}
		else {
			return node.get(domainIdKey).textValue();
		}
	}


	private static String getDomainIdKey(DataSource source) {
		return source
			.getDomainIdKey()
			.toString()
			.substring(1); //remove leading slash from domainIdKey in jsonPointer
	}
}
