package org.jvalue.ods.filter.reference;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.filter.FilterFactory;
import org.jvalue.ods.filter.description.FilterDescription;
import org.jvalue.ods.filter.description.FilterDescriptionManager;
import org.jvalue.ods.filter.description.FilterType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class FilterReferenceFactoryTest {

	private final FilterReference
			jsonSourceFilter = new FilterReference(FilterFactory.NAME_JSON_SOURCE_ADAPTER, new HashMap<String, Object>()),
			dbInsertionFilter = new FilterReference(FilterFactory.NAME_DB_INSERTION_FILTER, new HashMap<String, Object>()),
			notificationFilter = new FilterReference(FilterFactory.NAME_NOTIFICATION_FILTER, new HashMap<String, Object>());


	@Mocked FilterDescriptionManager descriptionManager;
	FilterReferenceFactory factory;


	@Before
	public void createFactory() {
		factory = new FilterReferenceFactory(descriptionManager);
	}


	@Test
	public void testCreateFilterReference(@Mocked final FilterDescription description) {

		final String referenceName = "someName";
		final Map<String, Object> args = new HashMap<>();
		args.put("key1", "hello world");
		args.put("key2", 42);
		args.put("key3", true);

		new Expectations() {{
			Map<String, Class<?>> argTypes = new HashMap<>();
			argTypes.put("key1", String.class);
			argTypes.put("key2", Integer.class);
			argTypes.put("key3", Boolean.class);

			descriptionManager.getByName(referenceName);
			result = description;

			description.getArgumentTypes();
			result = argTypes;
		}};

		FilterReference reference = factory.createFilterReference(referenceName, args);

		Assert.assertEquals(referenceName, reference.getName());
		Assert.assertEquals(args, reference.getArguments());
	}


	@Test(expected = FilterReferenceFactory.InvalidFilterException.class)
	public void testCreateInvalidReference(@Mocked final FilterDescription description) {

		final String referenceName = "someName";
		final Map<String, Object> args = new HashMap<>();
		args.put("key", "hello world");

		new Expectations() {{
			descriptionManager.getByName(referenceName);
			result = description;

			description.getArgumentTypes();
			result = new HashMap<String, Class<?>>();
		}};

		factory.createFilterReference(referenceName, args);
	}


	@Test
	public void testCreateFilterChainReference() {
		new Expectations() {{
			descriptionManager.getByName(FilterFactory.NAME_JSON_SOURCE_ADAPTER).getType();
			result = FilterType.OUTPUT_FILTER;

			descriptionManager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER).getType();
			result = FilterType.INPUT_OUTPUT_FILTER;

			descriptionManager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER).getType();
			result = FilterType.INPUT_OUTPUT_FILTER;

			FilterType.OUTPUT_FILTER.isValidNextFilter(FilterType.INPUT_OUTPUT_FILTER);
			result = true;
		}};

		String chainId = "someId";

		List<FilterReference> filterList = new LinkedList<>();
		filterList.add(jsonSourceFilter);
		filterList.add(dbInsertionFilter);
		filterList.add(notificationFilter);

		FilterChainExecutionInterval executionInterval = new FilterChainExecutionInterval(0, TimeUnit.SECONDS);

		FilterChainReference reference = factory.createFilterChainReference(chainId, filterList, executionInterval);
		Assert.assertEquals(chainId, reference.getFilterChainId());
		Assert.assertEquals(filterList, reference.getFilterReferences());
		Assert.assertEquals(executionInterval, reference.getExecutionInterval());
	}


	@Test(expected = FilterReferenceFactory.InvalidFilterException.class)
	public void testCreateEmptyFilterChainReference() {
		factory.createFilterChainReference("someId", new LinkedList<FilterReference>(), new FilterChainExecutionInterval(0, TimeUnit.SECONDS));
	}


	@Test(expected = FilterReferenceFactory.InvalidFilterException.class)
	public void testCreateInvalidFilterChainReference() {
		new Expectations() {{
			descriptionManager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER).getType();
			result = FilterType.INPUT_OUTPUT_FILTER;
		}};

		List<FilterReference> filterList = new LinkedList<>();
		filterList.add(dbInsertionFilter);
		factory.createFilterChainReference("someId", filterList, new FilterChainExecutionInterval(0, TimeUnit.SECONDS));
	}

}
