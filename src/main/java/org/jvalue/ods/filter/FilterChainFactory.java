package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.filter.description.FilterArgument;
import org.jvalue.ods.filter.description.FilterCreationMethod;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.filter.reference.FilterReference;
import org.jvalue.ods.utils.Assert;

import java.lang.annotation.Annotation;
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
			DataSource source,
			DataRepository dataRepository) {

		Assert.assertNotNull(chainReference, source, dataRepository);

		Filter<Void, ArrayNode> firstFilter = null;
		Filter<?, ArrayNode> lastFilter = null;

		for (FilterReference filterReference : chainReference.getFilterReferences()) {
			Filter filter = createFilterFromAnnotation(filterReference, source, dataRepository);
			if (firstFilter == null) {
				firstFilter = (Filter<Void, ArrayNode>) filter;
				lastFilter = (Filter<?, ArrayNode>) filter;
			} else {
				lastFilter = (Filter<?, ArrayNode>) lastFilter.setNextFilter(filter);
			}
		}

		return firstFilter;
	}


	@SuppressWarnings("rawTypes")
	private Filter createFilterFromAnnotation(
			FilterReference reference,
			DataSource dataSource,
			DataRepository dataRepository) {

		for (Method method : FilterFactory.class.getDeclaredMethods()) {
			FilterCreationMethod creationAnnotation = method.getAnnotation(FilterCreationMethod.class);
			if (creationAnnotation == null) throw new IllegalArgumentException("creation annotation not found");
			if (!reference.getName().equals(creationAnnotation.name())) continue;

			List<Object> arguments = new LinkedList<>();

			// add source and repository arguments
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
				else if (parameterType.equals(DataRepository.class)) arguments.add(dataRepository);
			}

			// add custom arguments
			Annotation[][] allParamAnnotations = method.getParameterAnnotations();
			for (Annotation[] paramAnnotations : allParamAnnotations) {
				for (Annotation  annotation : paramAnnotations) {
					if (annotation instanceof FilterArgument) {
						FilterArgument arg = (FilterArgument) annotation;
						arguments.add(reference.getArguments().get(arg.value()));
					}
				}
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
