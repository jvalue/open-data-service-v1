package jsonapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.utils.JsonMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResponseBody {

	private JsonNode data;
	private JsonNode links;
	private JsonNode error;

	@JsonCreator
	public ResponseBody (
		@JsonProperty("data") JsonNode data,
		@JsonProperty("links") JsonNode links,
		@JsonProperty("error") JsonNode error) {

		this.data = data;
		this.links = links;
		this.error = error;
	}


	public JsonNode getData() {
		return data;
	}


	public JsonNode getLinks() {
		return links;
	}


	public JsonNode getError() {
		return error;
	}


	public String getId() {
		return data.get("id").asText();
	}


	public String getType() {
		return data.get("type").asText();
	}


	public <T> T  dataToTargetObject(Class<T> valueType) {
		return doConvertAttributesToTargetObject(data, valueType);
	}


	public <T> List<T> dataToTargetObjectList(Class<T> valueType) {
		List<T> retList = new ArrayList<>();
		if(!data.isArray()) { //responses containing collections of only one element are serialized as single data objects
			retList.add(dataToTargetObject(valueType));
		}
		else {
			for (JsonNode node : data) {
				retList.add(doConvertAttributesToTargetObject(node, valueType));
			}
		}
		return retList;
	}


	private <T> T doConvertAttributesToTargetObject(JsonNode node, Class<T> valueType) {
		JsonNode attributes = node.get("attributes");
		if(hasStringField(valueType, "id")) {
			((ObjectNode) attributes).put("id", node.get("id").textValue());
		}
		if(hasStringField(valueType, "type")) {
			((ObjectNode) attributes).put("type", node.get("type").textValue());
		}
		return JsonMapper.convertValue(attributes, valueType);
	}


	private boolean hasStringField(Class clazz, String fieldName) {
		try {
			return isStringType(clazz.getDeclaredField(fieldName));
		} catch (NoSuchFieldException e) {
			return false;
		}
	}


	private boolean isStringType(Field field) {
		return field.getType().equals(String.class);
	}
}
