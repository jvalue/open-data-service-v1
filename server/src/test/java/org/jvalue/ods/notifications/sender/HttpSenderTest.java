package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

import mockit.integration.junit4.JMockit;
import retrofit.RetrofitError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JMockit.class)
public final class HttpSenderTest {

	private static final String SOURCE_ID = "someSourceId";

	private final DataSource source = new DataSource(SOURCE_ID, null, null, null);
	private HttpSender sender;


	@Test
	public final void testSuccess() throws Throwable {
		MockWebServer server = new MockWebServer();
		server.enqueue(new MockResponse().setResponseCode(200));
		server.start();

		String path = "/foo/bar/data/";
		String callbackUrl = server.getUrl(path).toString();

		ObjectNode sentData = new ObjectNode(JsonNodeFactory.instance);
		sentData.put("sourceId", SOURCE_ID);
		ArrayNode data = sentData.putArray("data");

		data.addObject().put("hello", "world");
		data.addObject().put("and", "again");

		setupSender(new HttpClient(SOURCE_ID, callbackUrl, true));

		sender.onNewDataStart();
		for (JsonNode item : data) sender.onNewDataItem((ObjectNode) item);
		sender.onNewDataComplete();

		assertEquals(SenderResult.Status.SUCCESS, sender.getSenderResult().getStatus());

		RecordedRequest request = server.takeRequest();
		assertEquals(path, request.getPath());
		assertEquals(sentData.toString(), request.getUtf8Body());
		server.shutdown();
	}


	@Test
	public final void testNoServerResponse() {
		setupSender(new HttpClient("dummy", "dummy", false));

		sender.onNewDataStart();
		sender.onNewDataItem(new ObjectNode(JsonNodeFactory.instance));
		sender.onNewDataComplete();

		SenderResult result = sender.getSenderResult();
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RetrofitError);
	}


	private void setupSender(HttpClient client) {
		sender = new HttpSender(source, client);
	}

}
