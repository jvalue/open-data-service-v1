package org.jvalue.ods.data;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.generic.DataSourceRepositoryFactory;
import org.jvalue.ods.db.couchdb.repositories.DataRepository;
import org.jvalue.ods.db.couchdb.repositories.DataSourceRepository;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;

import java.util.Arrays;
import java.util.Map;

@RunWith(JMockit.class)
public final class DataSourceManagerTest {

	private static final String SOURCE_ID = "someSourceId";

	@Mocked private DataSourceRepositoryFactory dataSourceFactory;

	@Mocked private DataSourceRepository sourceRepository;
	@Mocked private Cache<GenericDataRepository<CouchDbDataView, JsonNode>> dataRepositoryCache;
	@Mocked private DbConnectorFactory dbConnectorFactory;
	@Mocked private RepositoryFactory repositoryFactory;
	@Mocked private ProcessorChainManager processorChainManager;
	@Mocked private DataViewManager dataViewManager;
	@Mocked private NotificationManager notificationManager;
	@Mocked private DataRepository dataRepository;

	private DataSource dataSource;

	private DataSourceManager sourceManager;

	@Before
	public void setupSourceManager() {
        this.dataSource = new DataSource(
                SOURCE_ID,
                JsonPointer.compile("/id"),
                new ObjectNode(JsonNodeFactory.instance),
                new DataSourceMetaData("", "", "", "", "", "", ""));

		this.sourceManager = new DataSourceManager(
				dataSourceFactory,
				dataRepositoryCache,
				dbConnectorFactory,
				repositoryFactory,
				processorChainManager,
				dataViewManager,
				notificationManager);
	}


	@Test
	public void testStart() {
		new Expectations() {{
			sourceRepository.getAll();
			result = Arrays.asList(dataSource);

			repositoryFactory.createSourceDataRepository(SOURCE_ID, (JsonPointer) any);
			result = dataRepository;
		}};

		sourceManager.start();

		new Verifications() {{
			Map<DataSource, GenericDataRepository<CouchDbDataView, JsonNode>> sources;
			processorChainManager.startAllProcessorChains(sources = withCapture());
			Assert.assertTrue(sources.containsKey(dataSource));
		}};
	}


	@Test
	public void testStop() {
		sourceManager.stop();

		new Verifications() {{
			processorChainManager.stopAllProcessorChains();
		}};
	}


	@Test
	public void testAdd() {
		sourceManager.add(dataSource);

		new Verifications() {{
			repositoryFactory.createSourceDataRepository(SOURCE_ID, (JsonPointer) any);
			sourceRepository.add(dataSource);
		}};
	}


	@Test
	public void testRemove() {
		sourceManager.remove(dataSource);

		new Verifications() {{
			dataRepositoryCache.remove(SOURCE_ID);
			processorChainManager.removeAll(dataSource);
			dataViewManager.removeAll(dataSource);
			notificationManager.removeAll(dataSource);
			dbConnectorFactory.deleteDatabase(SOURCE_ID);
		}};
	}


	@Test
	public void testGetAll() {
		sourceManager.getAll();

		new Verifications() {{
			sourceRepository.getAll();
		}};
	}


	@Test
	public void testFindBySourceId() {
		sourceManager.findBySourceId(SOURCE_ID);

		new Verifications() {{
			sourceRepository.findById(SOURCE_ID);
		}};
	}

}
