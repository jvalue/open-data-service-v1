package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.utils.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public final class FilterChainFactory {

	private final FilterFactory filterFactory;

	@Inject
	public FilterChainFactory(FilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}


	public Filter<ArrayNode, ArrayNode> createFilterChain(
			FilterChainReference chainReference,
			DataSource dataSource,
			SourceDataRepository dataRepository) {

		Assert.assertNotNull(chainReference, dataSource, dataRepository);

		Filter<ArrayNode, ArrayNode> firstFilter = null;
		Filter<ArrayNode, ArrayNode> lastFilter = null;

		for (FilterReference filterReference : chainReference.getFilterReferences()) {
			Filter<ArrayNode, ArrayNode> filter = createFilterFromAnnotation(filterReference.getFilterKey(), dataSource, dataRepository);
			if (firstFilter == null) {
				firstFilter = filter;
				lastFilter = filter;
			} else {
				lastFilter = lastFilter.setNextFilter(filter);
			}
		}

		return firstFilter;
	}


	@SuppressWarnings("unchecked")
	private Filter<ArrayNode, ArrayNode> createFilterFromAnnotation(
			String annotationValue,
			DataSource dataSource,
			SourceDataRepository dataRepository) {

		for (Method method : FilterFactory.class.getDeclaredMethods()) {
			Named named = method.getAnnotation(Named.class);
			if (named == null) throw new IllegalArgumentException("named annotation not found");
			if (!annotationValue.equals(named.value())) continue;

			List<Object> arguments = new LinkedList<>();
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
				else if (parameterType.equals(SourceDataRepository.class)) arguments.add(dataRepository);
				else throw new IllegalStateException("what to do with parameter " + parameterType.toString());
			}



	/*
	public interface FilterFactory {

		static final String
				NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
				NAME_NOTIFICATION_FILTER = "NotificationFilter";

		@FilterName(NAME_NOTIFICATION_FILTER) @Named(NAME_NOTIFICATION_FILTER) public Filter<ArrayNode, ArrayNode> createNotificationFilter(DataSource source);
		@FilterName(NAME_DB_INSERTION_FILTER) @Named(NAME_DB_INSERTION_FILTER) public Filter<ArrayNode, ArrayNode> createDbInsertionFilter(
				DataSource source,
				SourceDataRepository dataRepository);

	}
	*/
			try {
				return (Filter<ArrayNode, ArrayNode>) method.invoke(filterFactory, arguments.toArray());
			} catch (IllegalAccessException | InvocationTargetException ie) {
				throw new IllegalStateException(ie);
			}
		}

		return null;
	}

}
