package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.utils.Assert;

import java.net.URL;

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


	private DataSource createSource(String id) throws Exception {
		return new DataSource(id,
				new URL("http://localhost/"),
				JsonPointer.compile("/domainId"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}

}
