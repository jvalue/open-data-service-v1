/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.processors.Specification;

import java.util.Collection;
import java.util.stream.Collectors;

@Schema(name = "specificationData")
public class SpecificationWrapper implements JsonApiIdentifiable {

	private final Specification specification;

	private SpecificationWrapper(Specification specification) {
		this.specification = specification;
	}

	@Schema(name = "attributes")
	@JsonUnwrapped
	@JsonIgnoreProperties({"id", "type"})
	public Specification getSpecification() {
		return specification;
	}


	@Schema(example = "NotificationFilter")
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
