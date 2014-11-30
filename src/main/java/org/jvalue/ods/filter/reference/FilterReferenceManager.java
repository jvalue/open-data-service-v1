package org.jvalue.ods.filter.reference;


import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.FilterFactory;
import org.jvalue.ods.utils.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FilterReferenceManager {

	private static final Map<String, FilterReference> filterReferences = new HashMap<>();

	static {
		for (Method method : FilterFactory.class.getDeclaredMethods()) {
			if (!Filter.class.isAssignableFrom(method.getReturnType())) continue;
			FilterAnnotation annotation = method.getAnnotation(FilterAnnotation.class);
			filterReferences.put(annotation.name(), new FilterReference(annotation.name(), annotation.filterType()));
		}
	}


	public Set<FilterReference> getAllFilterReferences() {
		return new HashSet<>(filterReferences.values());
	}


	public FilterReference getFilterReferenceByName(String name) {
		return filterReferences.get(name);
	}


	public FilterChainReference createFilterChainReference(
			String filterChainId,
			List<FilterReference> filterReferences,
			FilterChainMetaData metaData)
			throws InvalidFilterReferenceListException {

		Assert.assertNotNull(filterChainId, filterReferences, metaData);
		assertIsValidFilterReferenceList(filterReferences);
		return new FilterChainReference(filterChainId, filterReferences, metaData);
	}


	private void assertIsValidFilterReferenceList(List<FilterReference> references) throws InvalidFilterReferenceListException {
		if (references.isEmpty()) throw new InvalidFilterReferenceListException();
		if (!references.get(0).getFilterType().equals(FilterType.OUTPUT_FILTER)) throw new InvalidFilterReferenceListException(references.get(0));
		FilterReference lastReference =  null;
		for (FilterReference reference : references) {
			if (lastReference != null && !lastReference.getFilterType().isValidNextFilter(reference.getFilterType()))
				throw new InvalidFilterReferenceListException(lastReference, reference);
			lastReference = reference;
		}
	}


	public static class InvalidFilterReferenceListException extends Exception {

		InvalidFilterReferenceListException() {
			super("filter chain cannot be empty");
		}

		InvalidFilterReferenceListException(FilterReference firstReference) {
			super("filter chain must start with "  + FilterType.INPUT_FILTER + " but found "  + firstReference.getFilterType());
		}

		InvalidFilterReferenceListException(FilterReference filter1, FilterReference filter2) {
			super(filter1.getFilterType() + " cannot be followed by " + filter2.getFilterType());
		}
	}

}
