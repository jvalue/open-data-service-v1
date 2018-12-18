package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;
import org.jvalue.ods.utils.JsonMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

final public class MultiSourceAdapter implements SourceAdapter {

	private final SourceAdapterFactory adapterFactory;
	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final Map<String, SourceAdapter> adapterMap = new HashMap<>();


	@Inject
	public MultiSourceAdapter(
		SourceAdapterFactory adapterFactory,
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_MULTI_SOURCE) ArrayList<LinkedHashMap<String, Object>> sourceAdapters,
		MetricRegistry registry) {

		this.adapterFactory = adapterFactory;
		this.dataSource = dataSource;
		this.registry = registry;

		for (LinkedHashMap<String, Object> adapterItem : sourceAdapters) {
			String alias = adapterItem.get("alias").toString();
			ProcessorReference procRef = JsonMapper.convertValue(adapterItem.get("source"), ProcessorReference.class);
			SourceAdapter adapter = createAdapter(procRef);

			this.adapterMap.put(alias, adapter);
		}
	}


	@Override
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		List<ObjectNode> results = new ArrayList<>();

		ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
		for (Map.Entry<String, SourceAdapter> entry : adapterMap.entrySet()) {
			String alias = entry.getKey();
			SourceAdapter adapter = entry.getValue();

			JsonNode adapterNode = JsonMapper.valueToTree(Lists.newArrayList(adapter.iterator()));
			resultNode.set(alias, adapterNode);
		}

		results.add(resultNode);
		return results.iterator();
	}


	private SourceAdapter createAdapter(ProcessorReference reference) {
		Method method = getCreationMethod(reference.getName());

		List<Object> arguments = new LinkedList<>();
		arguments = addDataSourceAsFirstArgument(arguments, dataSource, method);
		arguments = addCustomArguments(arguments, reference, method);

		try {
			return (SourceAdapter) method.invoke(adapterFactory, arguments.toArray());

		} catch (IllegalAccessException | InvocationTargetException ie) {
			throw new IllegalStateException(ie);
		}
	}


	private Method getCreationMethod(String methodName) {
		Optional<Method> method = Arrays.stream(SourceAdapterFactory.class.getDeclaredMethods())
			.filter(m -> m.getAnnotation(CreationMethod.class).name().equals(methodName))
			.findAny();

		if (!method.isPresent()) {
			throw new IllegalArgumentException("Couldn't find creation method with annotation '" + methodName + "'");
		}

		return method.get();
	}


	private List<Object> addDataSourceAsFirstArgument(List<Object> arguments, DataSource dataSource, Method method) {
		for (Class<?> parameterType : method.getParameterTypes()) {
			if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
		}

		return arguments;
	}


	private List<Object> addCustomArguments(List<Object> arguments, ProcessorReference reference, Method method) {
		Annotation[][] allParamAnnotations = method.getParameterAnnotations();
		for (Annotation[] paramAnnotations : allParamAnnotations) {
			for (Annotation  annotation : paramAnnotations) {
				if (annotation instanceof Argument) {
					Argument arg = (Argument) annotation;
					arguments.add(reference.getArguments().get(arg.value()));
				}
			}
		}

		return arguments;
	}
}
