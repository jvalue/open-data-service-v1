package org.jvalue.ods.notifications.rest;

import org.jvalue.ods.notifications.RestAdapter;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;


public final class RestAdapterFactory {

	private RestAdapterFactory() { }


	private static GcmAdapter gcmAdapter;
	private static HttpAdapter restAdapter;


	public static RestAdapter<GcmClient> getGcmAdapter() {
		if (gcmAdapter == null) gcmAdapter = new GcmAdapter();
		return gcmAdapter;
	}


	public static RestAdapter<HttpClient> getRestAdapter() {
		if (restAdapter == null) restAdapter = new HttpAdapter();
		return restAdapter;
	}

}
