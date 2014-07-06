package org.jvalue.ods.notifications.rest;

import org.jvalue.ods.notifications.RestAdapter;
import org.jvalue.ods.notifications.clients.GcmClient;


public final class RestAdapterFactory {

	private RestAdapterFactory() { }


	private static GcmAdapter gcmAdapter;

	public static RestAdapter<GcmClient> getGcmAdapter() {
		if (gcmAdapter == null) gcmAdapter = new GcmAdapter();
		return gcmAdapter;
	}
}
