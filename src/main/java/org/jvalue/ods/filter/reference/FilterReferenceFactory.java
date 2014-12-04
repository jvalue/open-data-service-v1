package org.jvalue.ods.filter.reference;


import com.google.inject.Inject;

import org.jvalue.ods.filter.description.FilterDescription;
import org.jvalue.ods.filter.description.FilterDescriptionManager;
import org.jvalue.ods.filter.description.FilterType;
import org.jvalue.ods.utils.Assert;

import java.util.List;
import java.util.Map;

public final class FilterReferenceFactory {

	private final FilterDescriptionManager descriptionManager;

	@Inject
	public FilterReferenceFactory(FilterDescriptionManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	public FilterReference createFilterReference(String name, Map<String, Object> arguments) throws InvalidFilterException {
		Assert.assertNotNull(name, arguments);

		FilterDescription description = descriptionManager.getByName(name);
		if (description == null) throw new InvalidFilterException("no filter found for id \"" + name + "\"");
		if (arguments.size() != description.getArgumentTypes().size()) throw new InvalidFilterException("expected " + description.getArgumentTypes().size() + " arguments but got " + arguments.size());

		for (Map.Entry<String, Class<?>> entry : description.getArgumentTypes().entrySet()) {
			Object arg = arguments.get(entry.getKey());
			if (arg == null) throw new InvalidFilterException("no argument named \"" + entry.getKey() + "\" found");
			if (!entry.getValue().isAssignableFrom(arg.getClass())) throw new InvalidFilterException("invalid argument type for \"" + entry.getKey() + "\"");
		}

		return new FilterReference(name, arguments);
	}


	public FilterChainReference createFilterChainReference(
			String filterChainId,
			List<FilterReference> filterReferences,
			FilterChainExecutionInterval metaData)
			throws InvalidFilterException {

		Assert.assertNotNull(filterChainId, filterReferences, metaData);
		assertIsValidFilterReferenceList(filterReferences);
		return new FilterChainReference(filterChainId, filterReferences, metaData);
	}


	private void assertIsValidFilterReferenceList(List<FilterReference> references) throws InvalidFilterException {
		if (references.isEmpty())
			throw new InvalidFilterException("filter chain cannot be empty");
		if (!descriptionManager.getByName(references.get(0).getName()).getType().equals(FilterType.OUTPUT_FILTER))
			throw new InvalidFilterException("filter chain must start with " + FilterType.OUTPUT_FILTER);

		FilterReference lastReference =  null;
		for (FilterReference reference : references) {
			if (lastReference != null) {
				FilterType lastType = descriptionManager.getByName(lastReference.getName()).getType();
				FilterType nextType = descriptionManager.getByName(reference.getName()).getType();
				if (!lastType.isValidNextFilter(nextType)) throw new InvalidFilterException(lastType + " cannot be followed by " + nextType);
			}
			lastReference = reference;
		}
	}


	public static class InvalidFilterException extends IllegalArgumentException {

		InvalidFilterException(String msg) {
			super(msg);
		}

	}

}
