package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ProcessorChainReferenceRepositoryTest extends AbstractDbTest {

	private ProcessorChainReferenceRepository repository;

	public ProcessorChainReferenceRepositoryTest() {
		super(ProcessorChainReferenceRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		repository = new ProcessorChainReferenceRepository(couchDbInstance, databaseName);
	}


	@Test
	public void testFindById() throws Exception {
		repository.add(createChain("id1"));
		repository.add(createChain("id2"));

		Assert.assertNotNull(repository.findById("id1"));
		Assert.assertNotNull(repository.findById("id2"));
	}


	@Test(expected = DocumentNotFoundException.class)
	public void testInvalidId() {
		repository.findById("missingId");
	}


	@Test
	public void testGetAll() throws Exception {
		repository.add(createChain("id1"));
		repository.add(createChain("id2"));
		repository.add(createChain("id3"));

		Assert.assertEquals(3, repository.getAll().size());
	}


	private ProcessorReferenceChain createChain(String id) throws Exception {
		return new ProcessorReferenceChain(
				id,
				new LinkedList<ProcessorReference>(),
				new ExecutionInterval(0, TimeUnit.SECONDS));
	}

}
