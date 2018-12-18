package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.rest.v2.jsonapi.swagger.ExampleObjects;
import org.jvalue.ods.utils.JsonMapper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Schema(name = "data")
public class DataWrapper implements JsonApiIdentifiable{

	private final String id;
	private final Map<String, Object> attributes;

	private DataWrapper(JsonNode jsonNode, String domainIdKey) {
		this.id = getDomainId(jsonNode, domainIdKey);
		this.attributes = JsonMapper.convertValueToMap(jsonNode);
	}


	@Override
	@Schema(required = true, example = "0370")
	public String getId() {
		return id;
	}


	@Override
	@Schema(required = true, allowableValues = "Data")
	public String getType() {
		return Data.class.getSimpleName();
	}


	@JsonAnyGetter
	@JsonIgnoreProperties({"_id","_rev"})
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	/**
	 * Dummy method needed for generation of swagger documentation
	 * (Swagger does not consider @JsonAnyGetter annotated methods).
	 * @return null
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Schema(description = "dynamic set of properties depending on the dataSource and/or potential restrictions",
		name = "attributes",
		required = true,
		implementation = ExampleObjects.DataAttributesExample.class)
	public Map<String, String> getAttributesDummy() {
		return null;
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
			String dbId = node.get("_id").asText("NO_ID");
			Log.error("Node " + node.get("_id") + " has no domainId! It has been serialized using its database id.");
			return dbId;
		}
		else {
			return node.get(domainIdKey).asText();
		}
	}


	private static String getDomainIdKey(DataSource source) {
		return source
			.getDomainIdKey()
			.toString()
			.substring(1); //remove leading slash from domainIdKey in jsonPointer
	}
}
