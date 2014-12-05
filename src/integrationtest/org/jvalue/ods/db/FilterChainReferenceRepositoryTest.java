package org.jvalue.ods.db;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.filter.reference.FilterChainReference;

public class FilterChainReferenceRepositoryTest extends AbstractDbTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private FilterChainReferenceRepository repository;

	public FilterChainReferenceRepositoryTest() {
		super(FilterChainReferenceRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		repository = new FilterChainReferenceRepository(couchDbInstance, databaseName);
	}


	@Test
	public void testFindById() throws Exception {
		repository.add(createChain("id1"));
		repository.add(createChain("id2"));

		Assert.assertNotNull(repository.findByFilterChainId("id1"));
		Assert.assertNotNull(repository.findByFilterChainId("id2"));
	}


	@Test(expected = DocumentNotFoundException.class)
	public void testInvalidId() {
		repository.findByFilterChainId("missingId");
	}


	@Test
	public void testGetAll() throws Exception {
		repository.add(createChain("id1"));
		repository.add(createChain("id2"));
		repository.add(createChain("id3"));

		Assert.assertEquals(3, repository.getAll().size());
	}


	private FilterChainReference createChain(String id) throws Exception {
		String json = "{\"filterChainId\":\"" + id + "\","
				+ "\"filters\": [],"
				+ "\"executionInterval\":{"
				+ "\"period\":0, \"unit\":\"SECONDS\""
				+ "}}";
		return mapper.treeToValue(mapper.readTree(json), FilterChainReference.class);
	}

}
