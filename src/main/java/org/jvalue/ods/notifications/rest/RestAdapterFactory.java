package org.jvalue.ods.notifications.rest;

import org.jvalue.ods.notifications.RestAdapter;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.RestClient;


public final class RestAdapterFactory {

	private RestAdapterFactory() { }


	private static GcmAdapter gcmAdapter;
	private static RestNotificationAdapter restAdapter;


	public static RestAdapter<GcmClient> getGcmAdapter() {
		if (gcmAdapter == null) gcmAdapter = new GcmAdapter();
		return gcmAdapter;
	}


	public static RestAdapter<RestClient> getRestAdapter() {
		if (restAdapter == null) restAdapter = new RestNotificationAdapter();
		return restAdapter;
	}

}
