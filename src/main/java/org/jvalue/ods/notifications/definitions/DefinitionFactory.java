package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;


public final class DefinitionFactory {

	private DefinitionFactory() { }


	private static GcmDefinition gcmDefinition;
	private static HttpDefinition restDefinition;


	public static NotificationDefinition<GcmClient> getGcmDefinition() { 
		if (gcmDefinition == null) gcmDefinition = new GcmDefinition();
		return gcmDefinition;
	}


	public static NotificationDefinition<HttpClient> getRestDefinition() { 
		if (restDefinition == null) restDefinition = new HttpDefinition();
		return restDefinition;
	}

}
