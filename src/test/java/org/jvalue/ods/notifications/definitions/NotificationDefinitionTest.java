package org.jvalue.ods.notifications.definitions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class NotificationDefinitionTest {

	@Test
	public final void testGcmDefinition() {

		testDefinition(new GcmDefinition());

	}


	@Test
	public final void testHttpDefinition() {

		testDefinition(new HttpDefinition());

	}


	private void testDefinition(NotificationDefinition<?> definition) {

		assertNotNull(definition.getRestName());
		assertTrue(definition.getRestName().startsWith("/"));
		assertNotNull(definition.getRestAdapter());
		assertNotNull(definition.getNotificationSender());

	}

}
