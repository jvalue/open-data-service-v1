package org.jvalue.ods.processor.reference;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.FilterFactory;
import org.jvalue.ods.processor.specification.ProcessorType;
import org.jvalue.ods.processor.specification.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class ProcessorReferenceFactoryTest {

	private final ProcessorReference
			jsonSourceFilter = new ProcessorReference(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER, new HashMap<String, Object>()),
			dbInsertionFilter = new ProcessorReference(FilterFactory.NAME_DB_INSERTION_FILTER, new HashMap<String, Object>()),
			notificationFilter = new ProcessorReference(FilterFactory.NAME_NOTIFICATION_FILTER, new HashMap<String, Object>());


	@Mocked
	SpecificationManager descriptionManager;
	ProcessorReferenceFactory factory;


	@Before
	public void createFactory() {
		factory = new ProcessorReferenceFactory(descriptionManager);
	}


	@Test
	public void testCreateFilterReference(@Mocked final Specification description) {

		final String referenceName = "someName";
		final ProcessorReference reference = new ProcessorReference.Builder(referenceName)
				.argument("key1", "hello world")
				.argument("key2", 42)
				.argument("key3", true)
				.build();

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

		ProcessorReference validReference = factory.assertIsValidProcessorReference(reference);
		Assert.assertEquals(reference, validReference);
	}


	@Test(expected = ProcessorReferenceFactory.InvalidProcessorException.class)
	public void testCreateInvalidReference(@Mocked final Specification description) {

		final String referenceName = "someName";
		final ProcessorReference reference = new ProcessorReference.Builder(referenceName)
				.argument("key", "hello world")
				.build();

		new Expectations() {{
			descriptionManager.getByName(referenceName);
			result = description;

			description.getArgumentTypes();
			result = new HashMap<String, Class<?>>();
		}};

		factory.assertIsValidProcessorReference(reference);
	}


	@Test
	public void testCreateFilterChainReference() {
		new Expectations() {{
			descriptionManager.getByName(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER).getType();
			result = ProcessorType.SOURCE_ADAPTER;

			descriptionManager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER).getType();
			result = ProcessorType.FILTER;

			descriptionManager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER).getType();
			result = ProcessorType.FILTER;

			ProcessorType.SOURCE_ADAPTER.isValidNextFilter(ProcessorType.FILTER);
			result = true;
		}};

		String chainId = "someId";
		ProcessorReferenceChain reference = new ProcessorReferenceChain(
				chainId,
				Arrays.asList(jsonSourceFilter, dbInsertionFilter, notificationFilter),
				new ExecutionInterval(0, TimeUnit.SECONDS));


		ProcessorReferenceChain validReference = factory.assertIsValidReferenceChain(reference);
		Assert.assertEquals(reference, validReference);
	}


	@Test(expected = ProcessorReferenceFactory.InvalidProcessorException.class)
	public void testCreateEmptyFilterChainReference() {
		factory.assertIsValidReferenceChain(new ProcessorReferenceChain(
				"someId",
				new LinkedList<ProcessorReference>(),
				new ExecutionInterval(0, TimeUnit.SECONDS)));
	}


	@Test(expected = ProcessorReferenceFactory.InvalidProcessorException.class)
	public void testCreateInvalidFilterChainReference() {
		new Expectations() {{
			descriptionManager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER).getType();
			result = ProcessorType.FILTER;
		}};

		factory.assertIsValidReferenceChain(new ProcessorReferenceChain(
				"someId",
				Arrays.asList(dbInsertionFilter),
				new ExecutionInterval(0, TimeUnit.SECONDS)));
	}

}
