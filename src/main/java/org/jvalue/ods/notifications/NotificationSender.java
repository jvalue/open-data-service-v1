package org.jvalue.ods.notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jvalue.ods.logger.Logging;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public class NotificationSender {
	
	private static final int MULTICAST_SIZE = 1000;
	
	static final String 
		DATA_KEY_SOURCE = "source",
		DATA_KEY_DEBUG = "debug";

	
	private static final NotificationSender instance = new NotificationSender();
	public static NotificationSender getInstance() {
		return instance;
	}
	
	private final Executor threadPool = Executors.newFixedThreadPool(5);
	private final Sender sender;
	private NotificationSender() {
		sender = new Sender(ApiKey.getInstance().toString());
	}
	



	
	public int notifySourceChange(String source) throws IOException {
		Set<String> clients = ClientDatastore.getInstance().getRegisteredClients().get(source);
		if (clients == null) return 0;

		Map<String,String> payload = new HashMap<String,String>();
		payload.put(DATA_KEY_SOURCE, source);

		sendNotification(clients, payload, source);

		return clients.size();
	}



	void sendNotification(
			final Set<String> clients, 
			final Map<String,String> payload, 
			final String collapseKey) {

		Set<String> partialClients = new HashSet<String>(clients.size());
		int counter = 0;
		
		for (String client : clients) {
			counter++;
			partialClients.add(client);

			if (partialClients.size() == MULTICAST_SIZE || counter == clients.size()) {
				asyncSend(partialClients, payload, collapseKey);
				partialClients.clear();
			}
		}
	}


	private void asyncSend(
			final Set<String> partialClients, 
			final Map<String, String> payload,
			final String collapseKey) {

		final List<String> devices = new ArrayList<String>(partialClients);
		threadPool.execute(new Runnable() {
			
			public void run() {
				// send
				Message.Builder builder = new Message.Builder().collapseKey(collapseKey);
				for (Map.Entry<String, String> e : payload.entrySet()) {
					builder.addData(e.getKey(), e.getValue());
				}


				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(builder.build(), devices, 5);
				} catch (IOException e) {
					Logging.error(NotificationSender.class, "Error posting messages");
					return;
				}

				// analyze the results
				List<Result> results = multicastResult.getResults();
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						Logging.info(NotificationSender.class, "Succesfully sent message to device: " 
							+ regId + "; messageId = " + messageId);
						String canonicalRegId = result.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id: update it
							Logging.info(NotificationSender.class, "canonicalRegId " + canonicalRegId);
							ClientDatastore.getInstance().updateClientId(regId, canonicalRegId);
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device - unregister it
							Logging.info(NotificationSender.class, "Unregistered device: " + regId);
							ClientDatastore.getInstance().unregisterClient(regId);
						} else {
							Logging.error(NotificationSender.class, "Error sending message to " + regId + ": " + error);
						}
					}
				}
			}});
	}

}
