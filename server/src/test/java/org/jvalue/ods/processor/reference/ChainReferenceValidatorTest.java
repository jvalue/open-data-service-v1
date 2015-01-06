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

import javax.validation.ConstraintValidatorContext;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class ChainReferenceValidatorTest {

	private final ProcessorReference
			jsonSourceFilter = new ProcessorReference.Builder(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER)
					.argument("key1", "hello world")
					.build(),
			dbInsertionFilter = new ProcessorReference.Builder(FilterFactory.NAME_DB_INSERTION_FILTER)
					.argument("key2", 12)
					.build(),
			notificationFilter = new ProcessorReference.Builder(FilterFactory.NAME_NOTIFICATION_FILTER)
					.argument("key3", false)
					.build();

	@Mocked private ConstraintValidatorContext context;
	@Mocked private SpecificationManager descriptionManager;
	private ChainReferenceValidator validator;


	@Before
	public void createFactory() {
		validator = new ChainReferenceValidator(descriptionManager);
	}


	@Test
	public void testCreateFilterChainReference() {
		new Expectations() {{
			Map<String, Class<?>> argTypes = new HashMap<>();
			argTypes.put("key1", String.class);
			descriptionManager.getByName(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
			result = Deencapsulation.newInnerInstance(Specification.class, SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER, ProcessorType.SOURCE_ADAPTER, argTypes);

			argTypes = new HashMap<>();
			argTypes.put("key2", Integer.class);
			descriptionManager.getByName(FilterFactory.NAME_DB_INSERTION_FILTER);
			result = Deencapsulation.newInnerInstance(Specification.class, FilterFactory.NAME_DB_INSERTION_FILTER, ProcessorType.FILTER, argTypes);

			argTypes = new HashMap<>();
			argTypes.put("key3", Boolean.class);
			descriptionManager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER);
			result = Deencapsulation.newInnerInstance(Specification.class, FilterFactory.NAME_NOTIFICATION_FILTER, ProcessorType.FILTER, argTypes);
		}};

		String chainId = "someId";
		ProcessorReferenceChain reference = new ProcessorReferenceChain(
				chainId,
				Arrays.asList(jsonSourceFilter, dbInsertionFilter, notificationFilter),
				new ExecutionInterval(2, TimeUnit.SECONDS));

		Assert.assertTrue(validator.isValid(reference, context));
	}


	public void testCreateEmptyFilterChainReference() {
		Assert.assertFalse(validator.isValid(new ProcessorReferenceChain(
				"someId",
				new LinkedList<ProcessorReference>(),
				new ExecutionInterval(2, TimeUnit.SECONDS)), context));
	}


	public void testCreateInvalidFilterChainReference() {
		new Expectations() {{
			Map<String, Class<?>> argTypes = new HashMap<>();
			argTypes.put("key3", Boolean.class);
			descriptionManager.getByName(FilterFactory.NAME_NOTIFICATION_FILTER);
			result = Deencapsulation.newInnerInstance(Specification.class, FilterFactory.NAME_NOTIFICATION_FILTER, ProcessorType.FILTER, argTypes);
		}};

		Assert.assertFalse(validator.isValid(new ProcessorReferenceChain(
				"someId",
				Arrays.asList(dbInsertionFilter),
				new ExecutionInterval(2, TimeUnit.SECONDS)), context));
	}


	public void testMissingParameter() {
		new Expectations()
		{{
			final Map<String, Class<?>> argTypes = new HashMap<>();
			argTypes.put("key1", String.class);
			descriptionManager.getByName(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
			result = Deencapsulation.newInnerInstance(Specification.class, SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER, ProcessorType.SOURCE_ADAPTER, argTypes);
		}};

		Assert.assertFalse(validator.isValid(new ProcessorReferenceChain(
				"someId",
				Arrays.asList(jsonSourceFilter),
				new ExecutionInterval(2, TimeUnit.SECONDS)), context));
	}

}
