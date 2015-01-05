package org.jvalue.ods.db;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.processor.reference.ProcessorChainReference;

public class ProcessorChainReferenceRepositoryTest extends AbstractDbTest {

	private static final ObjectMapper mapper = new ObjectMapper();

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


	private ProcessorChainReference createChain(String id) throws Exception {
		String json = "{\"processorChainId\":\"" + id + "\","
				+ "\"processors\": [],"
				+ "\"executionInterval\":{"
				+ "\"period\":0, \"unit\":\"SECONDS\""
				+ "}}";
		return mapper.treeToValue(mapper.readTree(json), ProcessorChainReference.class);
	}

}
