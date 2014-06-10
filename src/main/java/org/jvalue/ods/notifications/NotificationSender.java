package org.jvalue.ods.notifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	private static final String DATA_KEY_SOURCE = "source";

	
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
		
		int total = clients.size();
		Set<String> partialClients = new HashSet<String>(total);
		int counter = 0;
		
		for (String client : clients) {
			counter++;
			partialClients.add(client);

			if (partialClients.size() == MULTICAST_SIZE || counter == total) {
				asyncSend(partialClients, source);
				partialClients.clear();
			}
		}

		return total;
	}


	private void asyncSend(Set<String> partialClients, final String source) {
		final List<String> devices = new ArrayList<String>(partialClients);
		threadPool.execute(new Runnable() {
			
			public void run() {
				// send
				Message message = new Message.Builder()
						.collapseKey(source)
						.addData(DATA_KEY_SOURCE, source)
						.build();
				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(message, devices, 5);
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
