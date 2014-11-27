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


	@SuppressWarnings("unchecked")
	public Filter<Void, ArrayNode> createFilterChain(
			FilterChainReference chainReference,
			DataSource dataSource,
			SourceDataRepository dataRepository) {

		Assert.assertNotNull(chainReference, dataSource, dataRepository);

		Filter<Void, ArrayNode> firstFilter = null;
		Filter<?, ArrayNode> lastFilter = null;

		for (FilterReference filterReference : chainReference.getFilterReferences()) {
			Filter filter = createFilterFromAnnotation(filterReference.getFilterKey(), dataSource, dataRepository);
			if (firstFilter == null) {
				firstFilter = (Filter<Void, ArrayNode>) filter;
				lastFilter = (Filter<?, ArrayNode>) filter;
			} else {
				System.out.println("setting last filter");
				if (lastFilter == null) System.out.println("last filter is null");
				if (filter == null) System.out.println("filter is null");
				lastFilter = (Filter<?, ArrayNode>) lastFilter.setNextFilter(filter);
			}
		}

		return firstFilter;
	}


	@SuppressWarnings("rawTypes")
	private Filter createFilterFromAnnotation(
			String annotationValue,
			DataSource dataSource,
			SourceDataRepository dataRepository) {

		for (Method method : FilterFactory.class.getDeclaredMethods()) {
			Named named = method.getAnnotation(Named.class);
			if (named == null) throw new IllegalArgumentException("named annotation not found");
			if (!annotationValue.equals(named.value())) continue;

			System.out.println("calling method " + method.getName());

			List<Object> arguments = new LinkedList<>();
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
				else if (parameterType.equals(SourceDataRepository.class)) arguments.add(dataRepository);
				else throw new IllegalStateException("what to do with parameter " + parameterType.toString());
			}

			try {
				return (Filter) method.invoke(filterFactory, arguments.toArray());
			} catch (IllegalAccessException | InvocationTargetException ie) {
				throw new IllegalStateException(ie);
			}
		}

		return null;
	}

}
