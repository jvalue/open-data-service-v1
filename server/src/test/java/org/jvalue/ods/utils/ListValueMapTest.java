package org.jvalue.ods.utils;

import org.junit.*;
import org.junit.Assert;

public final class ListValueMapTest {

	@Test
	public void testCrud() {
		ListValueMap<String, String> map = new ListValueMap<>();

		Assert.assertTrue(map.isEmpty());
		Assert.assertTrue(map.getAll().isEmpty());
		Assert.assertNull(map.get("key"));
		Assert.assertFalse(map.contains("key"));
		Assert.assertFalse(map.contains("key", "value"));
		Assert.assertFalse(map.remove("key"));
		Assert.assertFalse(map.remove("key", "value"));

		map.add("key", "value1");
		map.add("key", "value2");

		Assert.assertFalse(map.isEmpty());
		Assert.assertEquals(1, map.getAll().size());
		Assert.assertEquals(2, map.get("key").size());
		Assert.assertTrue(map.contains("key"));
		Assert.assertTrue(map.contains("key", "value1"));
		Assert.assertTrue(map.contains("key", "value2"));
		Assert.assertTrue(map.remove("key", "value1"));
		Assert.assertTrue(map.remove("key"));

		map.add("key1", "value");
		map.add("key2", "value");

		Assert.assertFalse(map.isEmpty());
		Assert.assertEquals(2, map.getAll().size());
		Assert.assertEquals(1, map.get("key1").size());
		Assert.assertEquals(1, map.get("key2").size());
		Assert.assertTrue(map.remove("key1"));
		Assert.assertTrue(map.remove("key2"));
		Assert.assertTrue(map.isEmpty());
	}

}
