package org.jvalue.ods.filter.reference;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.filter.FilterFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class FilterReferenceManagerTest {

	private final FilterReference
			jsonSourceFilter = new FilterReference(FilterFactory.NAME_JSON_SOURCE_ADAPTER, FilterType.OUTPUT_FILTER),
			dbInsertionFilter = new FilterReference(FilterFactory.NAME_DB_INSERTION_FILTER, FilterType.INPUT_OUTPUT_FILTER),
			notificationFilter = new FilterReference(FilterFactory.NAME_NOTIFICATION_FILTER, FilterType.INPUT_OUTPUT_FILTER);

	private FilterReferenceManager manager;


	@Before
	public void createManager() {
		manager = new FilterReferenceManager();
	}


	@Test
	public void testGetAll() {
		Set<FilterReference> expectedReferences = new HashSet<>();
		expectedReferences.add(jsonSourceFilter);
		expectedReferences.add(dbInsertionFilter);
		expectedReferences.add(notificationFilter);

		Set<FilterReference> references = manager.getAllFilterReferences();
		Assert.assertNotNull(references);
		for (FilterReference reference : references) {
			expectedReferences.remove(reference);
		}
		Assert.assertTrue(expectedReferences.isEmpty());
	}


	@Test
	public void testGetByName() {
		Assert.assertNotNull(manager.getFilterReferenceByName(FilterFactory.NAME_JSON_SOURCE_ADAPTER));
		Assert.assertNotNull(manager.getFilterReferenceByName(FilterFactory.NAME_DB_INSERTION_FILTER));
		Assert.assertNotNull(manager.getFilterReferenceByName(FilterFactory.NAME_NOTIFICATION_FILTER));
	}


	@Test
	public void testValidFilterChainReference() throws Exception {
		List<FilterReference> filterList = new LinkedList<>();
		filterList.add(jsonSourceFilter);
		filterList.add(dbInsertionFilter);
		filterList.add(notificationFilter);
		Assert.assertNotNull(manager.createFilterChainReference("someId", filterList, new FilterChainExecutionInterval(0, TimeUnit.SECONDS)));
	}


	@Test(expected = FilterReferenceManager.InvalidFilterReferenceListException.class)
	public void testEmptyFilterChainReference() throws Exception {
		List<FilterReference> filterList = new LinkedList<>();
		manager.createFilterChainReference("someId", filterList, new FilterChainExecutionInterval(0, TimeUnit.SECONDS));
	}


	@Test(expected = FilterReferenceManager.InvalidFilterReferenceListException.class)
	public void testInvalidFilterChainReference() throws Exception {
		List<FilterReference> filterList = new LinkedList<>();
		filterList.add(dbInsertionFilter);
		manager.createFilterChainReference("someId", filterList, new FilterChainExecutionInterval(0, TimeUnit.SECONDS));
	}

}
