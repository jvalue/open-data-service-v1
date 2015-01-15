package org.jvalue.ods.notifications.sender;


import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

public interface SenderFactory {

	public Sender<GcmClient> createGcmSender(DataSource source, GcmClient client);
	public Sender<HttpClient> createHttpSender(DataSource source, HttpClient client);

}
