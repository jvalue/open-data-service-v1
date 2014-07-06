package org.jvalue.ods.notifications.rest;

import org.jvalue.ods.notifications.RestAdapter;


public final class RestAdapterFactory {

	private RestAdapterFactory() { }


	private static GcmAdapter gcmAdapter;

	public static RestAdapter getGcmAdapter() {
		if (gcmAdapter == null) gcmAdapter = new GcmAdapter();
		return gcmAdapter;
	}
}
