package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.clients.HttpClient;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import retrofit.RetrofitError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(JMockit.class)
public final class HttpSenderTest {

	private static final String SOURCE_ID = "someSourceId";

	@Mocked
	private DataSource source;
	private final HttpSender sender = new HttpSender();


	@Before
	public void setupSource() {
		new Expectations() {{
			source.getId();
			result = SOURCE_ID;
		}};
	}


	@Test
	public final void testSuccess() throws Throwable {
		MockWebServer server = new MockWebServer();
		server.enqueue(new MockResponse().setResponseCode(200));
		server.play();

		String path = "/foo/bar/data/";
		String callbackUrl = server.getUrl(path).toString();
		ArrayNode data = new ArrayNode(JsonNodeFactory.instance);
		data.add(0);
		data.add(1);

		HttpClient client = new HttpClient(SOURCE_ID, callbackUrl, true);
		SenderResult result = sender.notifySourceChanged(client, source, data);

		assertEquals(SenderResult.Status.SUCCESS, result.getStatus());

		ObjectNode sentData = new ObjectNode(JsonNodeFactory.instance);
		sentData.put("sourceId", SOURCE_ID);
		sentData.put("data", data);
		RecordedRequest request = server.takeRequest();
		assertEquals(path, request.getPath());
		assertEquals(sentData.toString(), request.getUtf8Body());
		server.shutdown();
	}


	@Test
	public final void testFailNoData() {
		HttpClient client = new HttpClient("dummy", "dummy", false);
		SenderResult result = sender.notifySourceChanged(client, source, null);
		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RetrofitError);
	}


	@Test
	public final void testFail() {
		HttpClient client = new HttpClient("dummy", "dummy", true);
		SenderResult result = sender.notifySourceChanged(
				client,
				source,
				new ArrayNode(JsonNodeFactory.instance));
		assertNotNull(result);
		assertEquals(result.getStatus(), SenderResult.Status.ERROR);
		assertTrue(result.getErrorCause() instanceof RetrofitError);
	}

}
