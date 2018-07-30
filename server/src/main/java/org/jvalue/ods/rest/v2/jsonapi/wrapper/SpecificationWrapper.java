package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.ods.api.processors.ProcessorType;
import org.jvalue.ods.api.processors.Specification;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecificationWrapper implements JsonApiIdentifiable {

	private final Specification specification;

	private SpecificationWrapper(Specification specification) {
		this.specification = specification;
	}


	@Override
	public String getId() {
		return specification.getName();
	}


	@Override
	public String getType() {
		return specification.getType().name();
	}


	public static SpecificationWrapper from(Specification specification) {
		return new SpecificationWrapper(specification);
	}


	public static Collection<SpecificationWrapper> fromCollection(Collection<Specification> specs) {
		return specs.stream()
			.map(SpecificationWrapper::from)
			.collect(Collectors.toList());
	}
}
