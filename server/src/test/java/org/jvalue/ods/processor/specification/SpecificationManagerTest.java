package org.jvalue.ods.processor.specification;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.processors.Specification;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.FilterFactory;

import java.util.Set;

public final class SpecificationManagerTest {


	@Test
	public void testGetAll() {
		SpecificationManager manager = new SpecificationManager();
		Set<Specification> descriptions = manager.getAll();
		Assert.assertTrue(descriptions.size() > 0);
	}


	@Test
	public void testGetByName() {
		SpecificationManager manager = new SpecificationManager();
		Assert.assertNotNull(manager.getByName(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER));
		Assert.assertNotNull(manager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER));
		Assert.assertNotNull(manager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER));
	}

}
