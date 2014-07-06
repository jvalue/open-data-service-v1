package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.NotificationDefinition;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.RestClient;


public final class DefinitionFactory {

	private DefinitionFactory() { }


	private static GcmDefinition gcmDefinition;
	private static RestDefinition restDefinition;


	public static NotificationDefinition<GcmClient> getGcmDefinition() { 
		if (gcmDefinition == null) gcmDefinition = new GcmDefinition();
		return gcmDefinition;
	}


	public static NotificationDefinition<RestClient> getRestDefinition() { 
		if (restDefinition == null) restDefinition = new RestDefinition();
		return restDefinition;
	}

}
