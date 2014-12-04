package org.jvalue.ods.filter.description;


import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.FilterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FilterDescriptionManager {

	private static final Map<String, FilterDescription> descriptions = new HashMap<>();

	static {
		for (Method method : FilterFactory.class.getDeclaredMethods()) {
			if (!Filter.class.isAssignableFrom(method.getReturnType())) continue;

			// get filter name and type
			FilterCreationMethod creationAnnotation = method.getAnnotation(FilterCreationMethod.class);

			// get filter argument types
			Map<String, Class<?>> requiredFilterArgs = new HashMap<>();
			Annotation[][] argAnnotations = method.getParameterAnnotations();
			Class<?>[] argTypes = method.getParameterTypes();
			for (int i = 0; i < argAnnotations.length; ++i) {
				for (Annotation annotation : argAnnotations[i]) {
					if (annotation instanceof FilterArgument) {
						FilterArgument arg = (FilterArgument) annotation;
						requiredFilterArgs.put(arg.value(), argTypes[i]);
						break;
					}
				}
			}

			descriptions.put(
					creationAnnotation.name(),
					new FilterDescription(creationAnnotation.name(), creationAnnotation.filterType(), requiredFilterArgs));
		}
	}


	public Set<FilterDescription> getAll() {
		return new HashSet<>(descriptions.values());
	}


	public FilterDescription getByName(String name) {
		return descriptions.get(name);
	}

}
