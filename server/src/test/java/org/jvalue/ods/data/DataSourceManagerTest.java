package org.jvalue.ods.data;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.utils.Cache;

import java.util.Arrays;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class DataSourceManagerTest {

	private static final String SOURCE_ID = "someSourceId";

	@Mocked private DataSourceRepository sourceRepository;
	@Mocked private Cache<DataRepository> dataRepositoryCache;
	@Mocked private CouchDbInstance couchDbInstance;
	@Mocked private DbFactory dbFactory;
	@Mocked private ProcessorChainManager processorChainManager;
	@Mocked private DataViewManager dataViewManager;
	@Mocked private NotificationManager notificationManager;
	@Mocked private DataRepository dataRepository;

	private DataSource dataSource = new DataSource(
			SOURCE_ID,
			JsonPointer.compile("/id"),
			new ObjectNode(JsonNodeFactory.instance),
			new DataSourceMetaData("", "", "", "", "", "", ""));

	private DataSourceManager sourceManager;

	@Before
	public void setupSourceManager() {
		this.sourceManager = new DataSourceManager(
				sourceRepository,
				dataRepositoryCache,
				couchDbInstance,
				dbFactory,
				processorChainManager,
				dataViewManager,
				notificationManager);
	}


	@Test
	public void testStart() {
		new Expectations() {{
			sourceRepository.getAll();
			result = Arrays.asList(dataSource);

			dbFactory.createSourceDataRepository(SOURCE_ID, (JsonPointer) any);
			result = dataRepository;
		}};

		sourceManager.start();

		new Verifications() {{
			Map<DataSource, DataRepository> sources;
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
			dbFactory.createSourceDataRepository(SOURCE_ID, (JsonPointer) any);
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
			couchDbInstance.deleteDatabase(SOURCE_ID);
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
