package org.jvalue.ods.notifications.sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.clients.ClientFactory;
import org.jvalue.ods.notifications.clients.GcmClient;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;


final class GcmSender extends Sender<GcmClient> {
	
	static final String 
		DATA_KEY_SOURCE = "source",
		DATA_KEY_DEBUG = "debug";

	
	private final com.google.android.gcm.server.Sender sender;

	GcmSender(String apiKeyResource) {
		String apiKey = new GcmApiKey(apiKeyResource).toString();
		if (apiKey == null) sender = null;
		else sender = new com.google.android.gcm.server.Sender(apiKey);
	}
	
	
	@Override
	public SenderResult notifySourceChanged(
			GcmClient client, 
			DataSource source, 
			Object data) {

		if (sender == null) return getErrorResult("api key not set");

		// gather data
		Map<String,String> payload = new HashMap<String,String>();
		payload.put(DATA_KEY_SOURCE, source.getId());
		payload.put(DATA_KEY_DEBUG, Boolean.TRUE.toString());

		String collapseKey = source.getId();

		final List<String> devices = new ArrayList<String>();
		devices.add(client.getGcmClientId());

		// send
		Message.Builder builder = new Message.Builder().collapseKey(collapseKey);
		for (Map.Entry<String, String> e : payload.entrySet()) {
			builder.addData(e.getKey(), e.getValue());
		}

		MulticastResult multicastResult;
		try {
			multicastResult = sender.send(builder.build(), devices, 5);
		} catch (IOException io) {
			return getErrorResult(io);
		}

		// analyze the results
		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < devices.size(); i++) {
			String regId = devices.get(i);
			Result result = results.get(i);
			String messageId = result.getMessageId();
			if (messageId != null) {
				Logging.info(Sender.class, "Succesfully sent message to device: "
					+ regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					return getUpdateClientResult(
							client, 
							ClientFactory.newGcmClient(client.getSource(), canonicalRegId));
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					// application has been removed from device - unregister it
					return getRemoveClientResult(client);
				} else {
					return getErrorResult(error);
				}
			}
		}

		return getSuccessResult();
	}

}
