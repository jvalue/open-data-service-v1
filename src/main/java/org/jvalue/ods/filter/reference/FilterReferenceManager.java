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


	public FilterChainReference createFilterChainReference(String dataSourceId, List<FilterReference> filterReferences, FilterChainMetaData metaData) {
		Assert.assertNotNull(dataSourceId, filterReferences, metaData);
		Assert.assertTrue(isValidFilterReferenceList(filterReferences), "invalid filter list");
		return new FilterChainReference(dataSourceId, filterReferences, metaData);
	}


	private boolean isValidFilterReferenceList(List<FilterReference> filterReferences) {
		Assert.assertNotNull(filterReferences);
		if (filterReferences.get(0) == null || !filterReferences.get(0).getFilterType().equals(FilterType.OUTPUT_FILTER)) return false;
		FilterReference lastReference =  null;
		for (FilterReference filterReference : filterReferences) {
			if (filterReference == null) return false;
			if (lastReference != null && !lastReference.getFilterType().isValidNextFilter(filterReference.getFilterType())) return false;
			lastReference =  filterReference;
		}
		return true;
	}

}
