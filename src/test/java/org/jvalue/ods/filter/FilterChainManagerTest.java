package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.filter.reference.FilterChainReference;

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
public final class FilterChainManagerTest {

	private static final String FILTER_CHAIN_ID = "someFilterChainId";
	private static int filterRunCount;

	@Mocked private FilterChainFactory chainFactory;
	@Mocked private RepositoryCache<FilterChainReferenceRepository> repositoryCache;
	@Mocked private DbFactory dbFactory;
	private FilterChainManager manager;


	@Before
	public void resetFilterRunCount() {
		filterRunCount = 0;
	}


	@Before
	public void createFilterChainManager() {
		manager = new FilterChainManager(chainFactory, repositoryCache, dbFactory);
	}


	@Test
	public void testAddAndRemove(
			@Mocked final DataSource source,
			@Mocked final DataRepository dataRepository,
			@Mocked final FilterChainReference reference,
			@Mocked final FilterChainReferenceRepository referenceRepository) throws Exception {

		setupStartingFilterChain(source, dataRepository, referenceRepository, reference);

		// add chain
		manager.add(source, dataRepository, reference);
		Thread.sleep(50);
		Assert.assertEquals(1, filterRunCount);

		// remove chain
		manager.remove(source, dataRepository, reference);
		Thread.sleep(50);
		Assert.assertEquals(1, filterRunCount);

		new Verifications() {{
			// add chain
			dbFactory.createFilterChainReferenceRepository(anyString);
			referenceRepository.add(reference);
			repositoryCache.put(anyString, referenceRepository);

			// remove chain
			referenceRepository.remove(reference);
		}};
	}


	@Test
	public void testEmptyGet(
			@Mocked DataSource source,
			@Mocked final FilterChainReferenceRepository referenceRepository) {

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

		List<FilterChainReference> references = manager.getAll(source);
		Assert.assertNotNull(references);
		Assert.assertEquals(0, references.size());
	}


	@Test
	public void testEmptyChainExists(
			@Mocked DataSource source,
			@Mocked final FilterChainReferenceRepository referenceRepository) {

		new Expectations() {{
			repositoryCache.contains(anyString);
			result = true;
			repositoryCache.get(anyString);
			result = referenceRepository;
			referenceRepository.findByFilterChainId(anyString);
			result = new DocumentNotFoundException("");
		}};

		Assert.assertFalse(manager.contains(source, FILTER_CHAIN_ID));
	}


	@Test
	public void testStartAndStopAll(
			@Mocked final DataSource source,
			@Mocked final DataRepository dataRepository,
			@Mocked final FilterChainReference reference,
			@Mocked final FilterChainReferenceRepository referenceRepository) throws Exception {

		setupStartingFilterChain(source, dataRepository, referenceRepository, reference);
		new Expectations() {{
			referenceRepository.getAll();
			List<FilterChainReference> list = new LinkedList<>();
			list.add(reference);
			result = list;
		}};

		Map<DataSource, DataRepository> sources = new HashMap<>();
		sources.put(source, dataRepository);

		// start all
		manager.startAllFilterChains(sources);
		Thread.sleep(50);
		Assert.assertEquals(1, filterRunCount);

		// stop all
		manager.stopAllFilterChains();
		Thread.sleep(50);
		Assert.assertEquals(1, filterRunCount);
	}


	private void setupStartingFilterChain(
			final DataSource source,
			final DataRepository dataRepository,
			final FilterChainReferenceRepository referenceRepository,
			final FilterChainReference reference) {

		new Expectations() {{
			dbFactory.createFilterChainReferenceRepository(anyString);
			result = referenceRepository;

			reference.getExecutionInterval();
			result = new FilterChainExecutionInterval(75, TimeUnit.MILLISECONDS);

			chainFactory.createFilterChain(reference, source,  dataRepository);
			result = new DummyFilter();
		}};
	}


	private static final class DummyFilter extends Filter<Void, ArrayNode> {

		@Override
		protected ArrayNode doProcess(Void param) {
			filterRunCount++;
			return null;
		}


		@Override
		protected void doOnComplete() {
			// nothing to do here
		}

	}

}
