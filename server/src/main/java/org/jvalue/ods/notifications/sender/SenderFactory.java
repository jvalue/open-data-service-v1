package org.jvalue.ods.notifications.sender;


import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;

public interface SenderFactory {

	public Sender<GcmClient> createGcmSender(GcmClient client);
	public Sender<HttpClient> createHttpSender(HttpClient client);

}
