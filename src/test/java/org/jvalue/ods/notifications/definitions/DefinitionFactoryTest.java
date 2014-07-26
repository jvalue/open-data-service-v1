package org.jvalue.ods.notifications.definitions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class DefinitionFactoryTest {

	@Test
	public final void testGet() {

		NotificationDefinition<?> definition1 = DefinitionFactory.getGcmDefinition();
		assertNotNull(definition1);
		assertTrue(definition1 == DefinitionFactory.getGcmDefinition());

		NotificationDefinition<?> definition2 = DefinitionFactory.getRestDefinition();
		assertNotNull(definition2);
		assertTrue(definition2 == DefinitionFactory.getRestDefinition());

	}

}
