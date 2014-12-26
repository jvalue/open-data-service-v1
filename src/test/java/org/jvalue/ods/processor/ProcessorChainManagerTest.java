package org.jvalue.ods.processor;


import com.codahale.metrics.MetricRegistry;

import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.ProcessorChainReferenceRepository;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.processor.reference.ExecutionInterval;
import org.jvalue.ods.processor.reference.ProcessorChainReference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class ProcessorChainManagerTest {

	private static final String FILTER_CHAIN_ID = "someFilterChainId";

	@Mocked private ProcessorChainFactory chainFactory;
	@Mocked private RepositoryCache<ProcessorChainReferenceRepository> repositoryCache;
	@Mocked private DbFactory dbFactory;
	@Mocked private MetricRegistry registry;
	private ProcessorChainManager manager;


	@Before
	public void createFilterChainManager() {
		manager = new ProcessorChainManager(chainFactory, repositoryCache, dbFactory, registry);
	}


	@Test
	public void testAddAndRemove(
			@Mocked final DataSource source,
			@Mocked final DataRepository dataRepository,
			@Mocked final ProcessorChainReference reference,
			@Mocked final ProcessorChainReferenceRepository referenceRepository,
			@Mocked final ProcessorChain chain) throws Exception {

		setupStartingFilterChain(source, dataRepository, referenceRepository, reference, chain);

		// add chain
		manager.add(source, dataRepository, reference);
		Thread.sleep(50);

		// remove chain
		manager.remove(source, dataRepository, reference);
		Thread.sleep(50);

		new Verifications() {{
			// add chain
			dbFactory.createFilterChainReferenceRepository(anyString);
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
			@Mocked DataSource source,
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
	public void testEmptyGetAll(
			@Mocked DataSource source) {

		List<ProcessorChainReference> references = manager.getAll(source);
		Assert.assertNotNull(references);
		Assert.assertEquals(0, references.size());
	}


	@Test
	public void testEmptyChainExists(
			@Mocked DataSource source,
			@Mocked final ProcessorChainReferenceRepository referenceRepository) {

		new Expectations() {{
			repositoryCache.contains(anyString);
			result = true;
			repositoryCache.get(anyString);
			result = referenceRepository;
			referenceRepository.findByProcessorChainId(anyString);
			result = new DocumentNotFoundException("");
		}};

		Assert.assertFalse(manager.contains(source, FILTER_CHAIN_ID));
	}


	@Test
	public void testStartAndStopAll(
			@Mocked final DataSource source,
			@Mocked final DataRepository dataRepository,
			@Mocked final ProcessorChainReference reference,
			@Mocked final ProcessorChainReferenceRepository referenceRepository,
			@Mocked final ProcessorChain chain) throws Exception {

		setupStartingFilterChain(source, dataRepository, referenceRepository, reference, chain);
		new Expectations() {{
			referenceRepository.getAll();
			List<ProcessorChainReference> list = new LinkedList<>();
			list.add(reference);
			result = list;
		}};

		Map<DataSource, DataRepository> sources = new HashMap<>();
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
			final DataSource source,
			final DataRepository dataRepository,
			final ProcessorChainReferenceRepository referenceRepository,
			final ProcessorChainReference reference,
			final ProcessorChain chain) {

		new Expectations() {{
			dbFactory.createFilterChainReferenceRepository(anyString);
			result = referenceRepository;

			reference.getExecutionInterval();
			result = new ExecutionInterval(75, TimeUnit.MILLISECONDS);

			chainFactory.createProcessorChain(reference, source, dataRepository);
			result = chain;
		}};
	}

}
