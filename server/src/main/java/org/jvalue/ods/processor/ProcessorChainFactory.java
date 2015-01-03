package org.jvalue.ods.processor;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.processor.filter.FilterFactory;
import org.jvalue.ods.processor.reference.ProcessorChainReference;
import org.jvalue.ods.processor.reference.ProcessorReference;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;
import org.jvalue.ods.utils.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class ProcessorChainFactory {

	private final SourceAdapterFactory adapterFactory;
	private final FilterFactory filterFactory;

	@Inject
	public ProcessorChainFactory(
			SourceAdapterFactory adapterFactory,
			FilterFactory filterFactory) {

		this.adapterFactory = adapterFactory;
		this.filterFactory = filterFactory;
	}


	@SuppressWarnings("unchecked")
	public ProcessorChain createProcessorChain(
			ProcessorChainReference chainReference,
			DataSource source,
			DataRepository dataRepository) {

		Assert.assertNotNull(chainReference, source, dataRepository);

		SourceAdapter adapter;
		Filter<ObjectNode, ?> firstFilter = null;
		Filter<?, ObjectNode> lastFilter = null;

		Iterator<ProcessorReference> iterator = chainReference.getProcessors().iterator();
		adapter = (SourceAdapter) createProcessorFromAnnotation(adapterFactory, SourceAdapterFactory.class, iterator.next(), source, dataRepository);

		while (iterator.hasNext()) {
			Filter filter = (Filter) createProcessorFromAnnotation(filterFactory, FilterFactory.class, iterator.next(), source, dataRepository);
			if (firstFilter == null) {
				firstFilter = (Filter<ObjectNode, ObjectNode>) filter;
				lastFilter = (Filter<ObjectNode, ObjectNode>) filter;
			} else {
				lastFilter = (Filter<ObjectNode, ObjectNode>) lastFilter.setNextFilter(filter);
			}
		}

		return new ProcessorChain(adapter, firstFilter);
	}


	private Object createProcessorFromAnnotation(
			Object factory,
			Class<?> factoryClass, // hack as jmockit removed annotations from methods
			ProcessorReference reference,
			DataSource dataSource,
			DataRepository dataRepository) {

		for (Method method : factoryClass.getDeclaredMethods()) {
			CreationMethod creationAnnotation = method.getAnnotation(CreationMethod.class);
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
					if (annotation instanceof Argument) {
						Argument arg = (Argument) annotation;
						arguments.add(reference.getArguments().get(arg.value()));
					}
				}
			}

			try {
				return method.invoke(factory, arguments.toArray());
			} catch (IllegalAccessException | InvocationTargetException ie) {
				throw new IllegalStateException(ie);
			}
		}

		throw new IllegalStateException("failed to find creation method for name " + reference.getName());
	}

}
