package org.jvalue.ods.notifications.definitions;

import org.jvalue.ods.notifications.NotificationDefinition;
import org.jvalue.ods.notifications.clients.GcmClient;


public final class DefinitionFactory {

	private DefinitionFactory() { }


	private static GcmDefinition gcmDefinition;

	public static NotificationDefinition<GcmClient> getGcmDefinition() { 
		if (gcmDefinition == null) gcmDefinition = new GcmDefinition();
		return gcmDefinition;
	}

}
