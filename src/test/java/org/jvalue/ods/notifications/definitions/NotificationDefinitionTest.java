package org.jvalue.ods.notifications.definitions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jvalue.ods.notifications.sender.GcmApiKeyHelper;


public final class NotificationDefinitionTest {

	@Test
	public final void testGcmDefinition() {

		NotificationDefinition<?> definition = new GcmDefinition();
		assertNotNull(definition.getRestName());
		assertTrue(definition.getRestName().startsWith("/"));
		assertNotNull(definition.getRestAdapter());
		try {
			assertNotNull(definition.getNotificationSender());
		} catch (Exception e) {
			assertFalse(GcmApiKeyHelper.isApiKeyPresent());
		}

	}


	@Test
	public final void testHttpDefinition() {

		NotificationDefinition<?> definition = new HttpDefinition();
		assertNotNull(definition.getRestName());
		assertTrue(definition.getRestName().startsWith("/"));
		assertNotNull(definition.getRestAdapter());
		assertNotNull(definition.getNotificationSender());

	}

}
