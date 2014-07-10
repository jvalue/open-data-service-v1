package org.jvalue.ods.notifications.sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.clients.GcmClient;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


final class GcmSender extends NotificationSender<GcmClient> {
	
	static final String 
		DATA_KEY_SOURCE = "source",
		DATA_KEY_DEBUG = "debug";

	
	private final Sender sender;

	GcmSender() {
		String apiKey = GcmApiKey.getInstance().toString();
		if (apiKey == null) sender = null;
		else sender = new Sender(apiKey);
	}
	
	
	@Override
	public SenderResult notifySourceChanged(
			GcmClient client, 
			DataSource source, 
			GenericEntity data) {

		if (sender == null) return getErrorResult("api key not set");

		// gather data
		Map<String,String> payload = new HashMap<String,String>();
		payload.put(DATA_KEY_SOURCE, source.getId());
		payload.put(DATA_KEY_DEBUG, Boolean.TRUE.toString());

		String collapseKey = source.getId();

		final List<String> devices = new ArrayList<String>();
		devices.add(client.getId());

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
				Logging.info(NotificationSender.class, "Succesfully sent message to device: " 
					+ regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					return new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT)
						.oldClient(client)
						.newClient(new GcmClient(canonicalRegId, client.getSource()))
						.build();

				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					return new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
						.oldClient(client)
						.build();
					// application has been removed from device - unregister it
				} else {
					return getErrorResult(error);
				}
			}
		}

		return getSuccessResult();
	}

}
