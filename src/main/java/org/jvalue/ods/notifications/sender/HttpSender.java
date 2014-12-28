package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.HttpClient;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.http.Body;
import retrofit.http.POST;


public final class HttpSender extends Sender<HttpClient> {

	@Override
	public SenderResult notifySourceChanged(
			HttpClient client, 
			DataSource source, 
			ArrayNode data) {

		RestAdapter adapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint(client.getCallbackUrl())
				.build();
		NewDataCallbackService callbackService = adapter.create(NewDataCallbackService.class);

		NewData content;
		if (client.getSendData()) content = new NewData(source.getSourceId(), data);
		else content = new NewData(source.getSourceId(), null);

		try {
			callbackService.onNewData(content);
			return getSuccessResult();
		} catch (RetrofitError re) {
			return getErrorResult(re);
		}
	}


	/**
	 * The data (and metadata) that will be sent to the HTTP client.
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	private static final class NewData {

		public String sourceId;
		public JsonNode data;

		public NewData(String sourceId, JsonNode data) {
			this.sourceId = sourceId;
			this.data = data;
		}

	}


	/**
	 * Describes the REST endpoint that HTTP clients have to implement.
	 */
	private static interface NewDataCallbackService {

		@POST("/")
		public Response onNewData(@Body NewData data);

	}

}
