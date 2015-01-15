package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class GcmSenderTest {

	@Mocked private com.google.android.gcm.server.Sender gcmService;
	@Mocked private MulticastResult gcmMulticastResult;
	@Mocked private Result gcmResult;

	private final String sourceId = "someSourceId";
	private final DataSource source = new DataSource(sourceId, null, null, null);

	private final String gcmDeviceId = "someGcmDeviceId";
	private final GcmClient client = new GcmClient("someId", gcmDeviceId);
	private GcmSender sender;

	@Before
	public void setupSender() {
		sender = new GcmSender(client, gcmService);
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testGcmSuccess() throws Exception {
		new Expectations() {{
			gcmService.send((Message) any, (List) any, anyInt); result = gcmMulticastResult;
			gcmMulticastResult.getResults(); result = Arrays.asList(gcmResult);
			gcmResult.getMessageId(); result = "someMessageId";
		}};

		sender.onNewDataStart(source);
		sender.onNewDataItem(source, new ObjectNode(JsonNodeFactory.instance));
		sender.onNewDataComplete(source);

		Assert.assertEquals(SenderResult.Status.SUCCESS, sender.getSenderResult().getStatus());
		new Verifications() {{
			Message message;
			List<String> devices;
			gcmService.send(message = withCapture(), devices = withCapture(), anyInt);

			Assert.assertEquals(sourceId, message.getCollapseKey());
			Assert.assertEquals(sourceId, message.getData().get("source"));

			Assert.assertEquals(1, devices.size());
			Assert.assertTrue(devices.contains(gcmDeviceId));
		}};
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testGcmError() throws Exception {
		final IOException ioe = new IOException("boom");
		new Expectations() {{
			gcmService.send((Message) any, (List) any, anyInt);
			result = ioe;
		}};

		sender.onNewDataComplete(source);

		Assert.assertEquals(SenderResult.Status.ERROR, sender.getSenderResult().getStatus());
		Assert.assertEquals(ioe, sender.getSenderResult().getErrorCause());
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testGcmUpdate() throws Exception {
		final String newDeviceId = "someNewDeviceId";
		new Expectations() {{
			gcmService.send((Message) any, (List) any, anyInt); result = gcmMulticastResult;
			gcmMulticastResult.getResults(); result = Arrays.asList(gcmResult);
			gcmResult.getMessageId(); result = "someMessageId";
			gcmResult.getCanonicalRegistrationId(); result = newDeviceId;
		}};

		sender.onNewDataComplete(source);

		Assert.assertEquals(SenderResult.Status.UPDATE_CLIENT, sender.getSenderResult().getStatus());
		Assert.assertEquals(newDeviceId, ((GcmClient) sender.getSenderResult().getNewClient()).getGcmClientId());
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testGcmRemove() throws Exception {
		new Expectations() {{
			gcmService.send((Message) any, (List) any, anyInt); result = gcmMulticastResult;
			gcmMulticastResult.getResults(); result = Arrays.asList(gcmResult);
			gcmResult.getErrorCodeName(); result = Constants.ERROR_NOT_REGISTERED;
		}};

		sender.onNewDataComplete(source);

		Assert.assertEquals(SenderResult.Status.REMOVE_CLIENT, sender.getSenderResult().getStatus());
	}

}
