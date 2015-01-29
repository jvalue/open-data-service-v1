package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.List;

public abstract class AbstractRepositoryAdapterTest<T> extends AbstractRepositoryTest {

	private RepositoryAdapter<?, ?, T> repository;

	public AbstractRepositoryAdapterTest(String databaseName) {
		super(databaseName);
	}


	@Override
	protected final void doCreateDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		this.repository = doCreateAdapter(couchDbInstance, databaseName);
	}


	@Test
	public final void testFindById() throws Exception {
		T value1 = doCreateValue("id1", "");
		T value2 = doCreateValue("id2", "");
		repository.add(value1);
		repository.add(value2);

		Assert.assertEquals(value1, repository.findById("id1"));
		Assert.assertEquals(value2, repository.findById("id2"));
	}


	@Test(expected = DocumentNotFoundException.class)
	public void testInvalidId() {
		repository.findById("missingId");
	}


	@Test
	public void testGetAll() throws Exception {
		repository.add(doCreateValue("id1", ""));
		repository.add(doCreateValue("id2", ""));

		List<T> values = repository.getAll();
		Assert.assertEquals(2, values.size());
	}


	@Test
	public void testRemove() {
		repository.add(doCreateValue("id1", ""));
		repository.remove(repository.findById("id1"));
		try {
			repository.findById("id1");
		} catch(DocumentNotFoundException dnfe) {
			return;
		}
		Assert.fail();
	}


	@Test
	public void testUpdate() {
		repository.add(doCreateValue("id1", "hello"));
		T updatedValue = doCreateValue("id1", "world");
		repository.update(updatedValue);
		Assert.assertEquals(updatedValue, repository.findById("id1"));
	}


	protected abstract RepositoryAdapter<?, ?, T> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName);
	protected abstract T doCreateValue(String id, String data);
}
