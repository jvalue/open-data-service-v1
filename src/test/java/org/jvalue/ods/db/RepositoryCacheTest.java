package org.jvalue.ods.db;


import org.ektorp.support.CouchDbRepositorySupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class RepositoryCacheTest {

	@Test
	public void testCrud(@Mocked CouchDbRepositorySupport<Object> repository) {

		RepositoryCache<CouchDbRepositorySupport<Object>> cache = new RepositoryCache<>();

		Assert.assertEquals(0, cache.getAll().size());

		cache.put("key1", repository);
		Assert.assertEquals(1, cache.getAll().size());
		Assert.assertNotNull(cache.getForKey("key1"));

		cache.put("key2", repository);
		Assert.assertEquals(2, cache.getAll().size());
		Assert.assertNotNull(cache.getForKey("key2"));

		cache.remove("key2");
		Assert.assertEquals(1, cache.getAll().size());
		Assert.assertNull(cache.getForKey("key2"));

		cache.remove("key1");
		Assert.assertEquals(0, cache.getAll().size());
		Assert.assertNull(cache.getForKey("key1"));
	}

}
