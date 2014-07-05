package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.notifications.ApiKey;
import org.jvalue.ods.notifications.NotificationSender;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.utils.Assert;


public final class SenderFactory {

	private SenderFactory() { }


	private static GcmSender gcmSender;

	public static NotificationSender<GcmClient> getGcmSender(ApiKey apiKey) {
		Assert.assertNotNull(apiKey);
		if (gcmSender == null) gcmSender = new GcmSender(apiKey);
		return gcmSender;
	}
}
