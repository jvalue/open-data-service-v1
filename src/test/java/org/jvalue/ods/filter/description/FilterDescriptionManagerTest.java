package org.jvalue.ods.filter.description;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.filter.FilterFactory;

import java.util.Set;

public final class FilterDescriptionManagerTest {


	@Test
	public void testGetAll() {
		FilterDescriptionManager manager = new FilterDescriptionManager();
		Set<FilterDescription> descriptions = manager.getAll();
		Assert.assertTrue(descriptions.size() > 0);
	}


	@Test
	public void testGetByName() {
		FilterDescriptionManager manager = new FilterDescriptionManager();
		Assert.assertNotNull(manager.getByName(FilterFactory.NAME_JSON_SOURCE_ADAPTER));
		Assert.assertNotNull(manager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER));
		Assert.assertNotNull(manager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER));
	}

}
