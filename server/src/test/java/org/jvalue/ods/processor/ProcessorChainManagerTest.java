package org.jvalue.ods.processor;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.repositories.ProcessorChainReferenceRepository;
import org.jvalue.ods.db.couchdb.RepositoryFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;

@RunWith(JMockit.class)
public final class ProcessorChainManagerTest {

	private static final String FILTER_CHAIN_ID = "someFilterChainId";

	private final DataSource source = new DataSource(
			"someId",
			JsonPointer.compile("/id"),
			new ObjectNode(JsonNodeFactory.instance),
			new DataSourceMetaData("", "", "", "", "", "", ""));
	@Mocked private ProcessorChainFactory chainFactory;
	@Mocked private Cache<GenericRepository<ProcessorReferenceChain>> repositoryCache;
	@Mocked private RepositoryFactory repositoryFactory;
	@Mocked private MetricRegistry registry;
	private ProcessorChainManager manager;

	private final ProcessorReferenceChain reference = new ProcessorReferenceChain(
			"someId",
			new LinkedList<ProcessorReference>(),
			new ExecutionInterval(75, TimeUnit.MILLISECONDS));

	@Before
	public void createFilterChainManager() {
		manager = new ProcessorChainManager(chainFactory, repositoryCache, repositoryFactory, registry);
	}


	@Test
	public void testAddAndRemove(
		@Mocked final GenericDataRepository<CouchDbDataView, JsonNode> dataRepository,
		@Mocked final ProcessorChainReferenceRepository referenceRepository,
		@Mocked final ProcessorChain chain) throws Exception {

		setupStartingFilterChain(dataRepository, referenceRepository, chain);

		// add chain
		manager.add(source, dataRepository, reference);
		Thread.sleep(50);

		// remove chain
		manager.remove(source, dataRepository, reference);
		Thread.sleep(50);

		new Verifications() {{
			// add chain
			repositoryFactory.createFilterChainReferenceRepository(anyString);
			referenceRepository.add(reference);
			repositoryCache.put(anyString, referenceRepository);

			// remove chain
			referenceRepository.remove(reference);

			// chain started once
			chain.startProcessing();
			times = 1;
		}};
	}


	@Test
	public void testEmptyGet(
			@Mocked final ProcessorChainReferenceRepository referenceRepository) {

		new Expectations() {{
			repositoryCache.contains(anyString);
			result = true;
			repositoryCache.get(anyString);
			result = referenceRepository;
		}};

		manager.get(source, FILTER_CHAIN_ID);

		new Verifications() {{
			repositoryCache.get(anyString);
			times = 1;
		}};
	}


	@Test
	public void testEmptyGetAll() {

		List<ProcessorReferenceChain> references = manager.getAll(source);
		Assert.assertNotNull(references);
		Assert.assertEquals(0, references.size());
	}


	@Test
	public void testEmptyChainExists(
			@Mocked final ProcessorChainReferenceRepository referenceRepository) {

		new Expectations() {{
			repositoryCache.contains(anyString);
			result = true;
			repositoryCache.get(anyString);
			result = referenceRepository;
			referenceRepository.findById(anyString);
			result = new DocumentNotFoundException("");
		}};

		Assert.assertFalse(manager.contains(source, FILTER_CHAIN_ID));
	}


	@Test
	public void testStartAndStopAll(
			@Mocked final GenericDataRepository<CouchDbDataView, JsonNode> dataRepository,
			@Mocked final ProcessorChainReferenceRepository referenceRepository,
			@Mocked final ProcessorChain chain) throws Exception {

		setupStartingFilterChain(dataRepository, referenceRepository, chain);
		new Expectations() {{
			referenceRepository.getAll();
			List<ProcessorReferenceChain> list = new LinkedList<>();
			list.add(reference);
			result = list;
		}};

		Map<DataSource, GenericDataRepository<CouchDbDataView, JsonNode>> sources = new HashMap<>();
		sources.put(source, dataRepository);

		// start all
		manager.startAllProcessorChains(sources);
		Thread.sleep(50);

		// stop all
		manager.stopAllProcessorChains();
		Thread.sleep(50);

		new Verifications() {{
			chain.startProcessing();
			times = 1;
		}};
	}


	private void setupStartingFilterChain(
			final GenericDataRepository<CouchDbDataView, JsonNode> dataRepository,
			final ProcessorChainReferenceRepository referenceRepository,
			final ProcessorChain chain) {

		new Expectations() {{
			repositoryFactory.createFilterChainReferenceRepository(anyString);
			result = referenceRepository;

			chainFactory.createProcessorChain(reference, source, dataRepository);
			result = chain;
		}};
	}

}
