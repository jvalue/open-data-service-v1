package jsonapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.utils.JsonMapper;

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


	public <T> T  dataToTargetObject(Class<T> valueType) {
		JsonNode node =data.get("attributes");
		return JsonMapper.convertValue(node, valueType);
	}
}
