package org.jvalue.ods.notifications.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class RestAdapterFactoryTest {

	@Test
	public void testGet() {

		RestAdapter<?> adapter1 = RestAdapterFactory.getGcmAdapter();
		assertNotNull(adapter1);
		assertTrue(adapter1 == RestAdapterFactory.getGcmAdapter());

		RestAdapter<?> adapter2 = RestAdapterFactory.getRestAdapter();
		assertNotNull(adapter2);
		assertTrue(adapter2 == RestAdapterFactory.getRestAdapter());

	}

}
