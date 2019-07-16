/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


final class GcmSender extends AbstractSender<GcmClient> {
	
	static final String 
		DATA_KEY_SOURCE = "source",
		DATA_KEY_DEBUG = "debug";

	
	private final com.google.android.gcm.server.Sender gcmSender;

	@Inject
	GcmSender(
			@Assisted DataSource source,
			@Assisted GcmClient client,
			com.google.android.gcm.server.Sender gcmSender) {

		super(source, client);
		this.gcmSender = gcmSender;
	}


	@Override
	public void onNewDataStart() {
		// nothing to do here
	}


	@Override
	public void onNewDataItem(ObjectNode data) {
		// nothing to do here
	}


	@Override
	public void onNewDataComplete() {
		// gather data
		Map<String,String> payload = new HashMap<>();
		payload.put(DATA_KEY_SOURCE, source.getId());
		payload.put(DATA_KEY_DEBUG, Boolean.TRUE.toString());

		String collapseKey = source.getId();

		final List<String> devices = new ArrayList<>();
		devices.add(client.getGcmClientId());

		// send
		Message.Builder builder = new Message.Builder().collapseKey(collapseKey);
		for (Map.Entry<String, String> e : payload.entrySet()) {
			builder.addData(e.getKey(), e.getValue());
		}

		MulticastResult multicastResult;
		try {
			multicastResult = gcmSender.send(builder.build(), devices, 5);
		} catch (IOException io) {
			setErrorResult(io);
			return;
		}

		// analyze the results
		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < devices.size(); i++) {
			String regId = devices.get(i);
			Result result = results.get(i);
			String messageId = result.getMessageId();
			if (messageId != null) {
				Log.info("Succesfully sent message to device: "
						+ regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					setUpdateClientResult(
							client,
							new GcmClient(client.getId(), canonicalRegId));
					return;
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					// application has been removed from device - unregister it
					setRemoveClientResult(client);
					return;
				} else {
					setErrorResult(error);
					return;
				}
			}
		}

		setSuccessResult();
	}

}
