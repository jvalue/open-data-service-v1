package org.jvalue.common.db;


import org.ektorp.support.CouchDbRepositorySupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class RepositoryAdapterTest {

	@Mocked private DummyRepositorySupport repository;
	private RepositoryAdapter<DummyRepositorySupport, DbDocument<String>, String> adapter;

	@Before
	public void setupAdapter() {
		adapter = new RepositoryAdapter<DummyRepositorySupport, DbDocument<String>, String>(repository) { };
	}


	@Test
	public void testFindById() {
		final String id = "someId";
		final String data = "someData";

		new Expectations() {{
			repository.findById(id);
			result = new DbDocument<>(data);
		}};

		String resultData = adapter.findById(id);
		Assert.assertEquals(data, resultData);
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testAdd() {
		final String data = "someData";

		adapter.add(data);

		new Verifications() {{
			repository.createDbDocument(data); times = 1;
			repository.add((DbDocument<String>) any); times = 1;
		}};
	}


	@Test
	public void testUpdate() {
		final String id = "someId";
		final String oldData = "someOlData";
		final String newData = "someNewData";

		new Expectations() {{
			repository.getIdForValue(newData);
			result = id;

			repository.findById(id);
			result = new DbDocument<>(oldData);
		}};

		adapter.update(newData);

		new Verifications() {{
			DbDocument<String> doc;
			repository.update(doc = withCapture());
			Assert.assertEquals(newData, doc.getValue());
		}};
	}


	@Test
	public void testRemove() {
		final String id = "someId";
		final String data = "someData";

		new Expectations() {{
			repository.getIdForValue(data);
			result = id;

			repository.findById(id);
			result = new DbDocument<>(data);
		}};

		adapter.remove(data);

		new Verifications() {{
			DbDocument<String> doc;
			repository.remove(doc = withCapture());
			Assert.assertEquals(data, doc.getValue());
		}};
	}


	@Test
	public void testGetAll() {
		final String id = "someId";
		final List<String> data = Arrays.asList("data1", "data2");

		new Expectations() {{
			List<DbDocument<String>> docData = new LinkedList<>();
			for (String d : data) docData.add(new DbDocument<>(d));
			repository.getAll();
			result = docData;
		}};

		List<String> resultData = adapter.getAll();
		Assert.assertEquals(data, resultData);
	}


	public static abstract class DummyRepositorySupport
			extends CouchDbRepositorySupport<DbDocument<String>>
			implements DbDocumentAdaptable<DbDocument<String>, String> {


		DummyRepositorySupport() {
			super(null, null);
		}

		// somehow JMockit is loosing the type information about paramters which
		// makes the test fail due to methods not beeing found --> "implement" here
		public abstract DbDocument<String> findById(String id);
		public abstract DbDocument<String> createDbDocument(String value);

	}

}
