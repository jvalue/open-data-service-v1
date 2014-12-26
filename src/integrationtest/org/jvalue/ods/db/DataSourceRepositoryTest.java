package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceMetaData;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSourceRepositoryTest extends AbstractDbTest {

	private DataSourceRepository repository;

	public DataSourceRepositoryTest() {
		super(DataSourceRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		repository = new DataSourceRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Test
	public void testFindBySourceId() throws Exception {
		repository.add(createSource("id1"));
		repository.add(createSource("id2"));

		Assert.assertNotNull(repository.findBySourceId("id1"));
		Assert.assertNotNull(repository.findBySourceId("id2"));
	}


	@Test(expected = DocumentNotFoundException.class)
	public void testInvalidId() {
		repository.findBySourceId("missingId");
	}


	@Test
	public void testGetAll() throws Exception {
		repository.add(createSource("id1"));
		repository.add(createSource("id2"));

		List<DataSource> sources = repository.getAll();
		Assert.assertEquals(2, sources.size());

		Set<String> ids = new HashSet<>(Arrays.asList("id1", "id2"));
		for (DataSource source : sources) {
			Assert.assertTrue(ids.remove(source.getSourceId()));
		}
		Assert.assertTrue(ids.isEmpty());
	}


	private DataSource createSource(String id) throws Exception {
		return new DataSource(id,
				new URL("http://localhost/"),
				JsonPointer.compile("/domainId"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}

}
