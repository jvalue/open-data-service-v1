package org.jvalue.ods.utils;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class CacheTest {

	@Test
	public void testCrud() {

		Cache<String> cache = new Cache<>();

		Assert.assertEquals(0, cache.getAll().size());

		cache.put("key1", "hello");
		Assert.assertEquals(1, cache.getAll().size());
		Assert.assertEquals("hello", cache.get("key1"));

		cache.put("key2", "world");
		Assert.assertEquals(2, cache.getAll().size());
		Assert.assertEquals("world", cache.get("key2"));

		cache.remove("key2");
		Assert.assertEquals(1, cache.getAll().size());
		Assert.assertNull(cache.get("key2"));

		cache.remove("key1");
		Assert.assertEquals(0, cache.getAll().size());
		Assert.assertNull(cache.get("key1"));
	}

}
