package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.http.Body;
import retrofit.http.POST;


final class HttpSender extends AbstractSender<HttpClient> {

	private final ArrayNode buffer = new ArrayNode(JsonNodeFactory.instance);

	@Inject
	public HttpSender(@Assisted DataSource source, @Assisted HttpClient client) {
		super(source, client);
	}


	@Override
	public void onNewDataStart() {
		// nothing to do
	}


	@Override
	public void onNewDataItem(ObjectNode data) {
		if (client.getSendData()) buffer.add(data);
	}


	@Override
	public void onNewDataComplete() {
		RestAdapter adapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint(client.getCallbackUrl())
				.build();
		NewDataCallbackService callbackService = adapter.create(NewDataCallbackService.class);

		NewData content;
		if (client.getSendData()) content = new NewData(source.getId(), buffer);
		else content = new NewData(source.getId(), null);

		try {
			callbackService.onNewData(content);
			buffer.removeAll();
			setSuccessResult();
		} catch (RetrofitError re) {
			setErrorResult(re);
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
