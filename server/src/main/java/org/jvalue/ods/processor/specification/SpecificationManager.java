/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.specification;


import org.jvalue.ods.api.processors.Specification;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.processor.filter.FilterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SpecificationManager {

	private static final Map<String, Specification> descriptions = new HashMap<>();

	static {
		processFactory(SourceAdapterFactory.class, SourceAdapter.class);
		processFactory(FilterFactory.class, Filter.class);
	}


	public Set<Specification> getAll() {
		return new HashSet<>(descriptions.values());
	}


	public Specification getByName(String name) {
		return descriptions.get(name);
	}


	private static void processFactory(Class<?> factoryClass, Class<?> targetClass) {
		for (Method method : factoryClass.getDeclaredMethods()) {
			if (!targetClass.isAssignableFrom(method.getReturnType())) continue;

			// get filter name and type
			CreationMethod creationAnnotation = method.getAnnotation(CreationMethod.class);

			// get filter argument types
			Map<String, Class<?>> requiredFilterArgs = new HashMap<>();
			Annotation[][] argAnnotations = method.getParameterAnnotations();
			Class<?>[] argTypes = method.getParameterTypes();
			for (int i = 0; i < argAnnotations.length; ++i) {
				for (Annotation annotation : argAnnotations[i]) {
					if (annotation instanceof Argument) {
						Argument arg = (Argument) annotation;
						requiredFilterArgs.put(arg.value(), argTypes[i]);
						break;
					}
				}
			}

			descriptions.put(
					creationAnnotation.name(),
					new Specification(creationAnnotation.name(), creationAnnotation.filterType(), requiredFilterArgs));
		}
	}

}
