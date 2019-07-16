/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.transformation.DataTransformationManager;
import org.jvalue.ods.transformation.TransformationFunction;

public class TransformationFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	private final String transformationFunction;
	private final DataTransformationManager dataTransformationManager;


	@Inject
	public TransformationFilter(
		@Assisted DataSource source,
		MetricRegistry registry,
		DataTransformationManager dataTransformationManager,
		@Assisted String transformationFunction) {
		super(source, registry);
		this.transformationFunction = transformationFunction;
		this.dataTransformationManager = dataTransformationManager;
	}


	@Override
	protected ObjectNode doFilter(ObjectNode node) throws FilterException {
		ObjectNode result;
		try {
			result = dataTransformationManager.transform(node, new TransformationFunction(source.getId(), transformationFunction));
		} catch (Exception e) {
			// nothing to do if exception occurs -> throw it.
			throw new FilterException(e.getMessage(), e);
		}

		return result;
	}


	@Override
	protected void doOnComplete() throws FilterException {
	}
}
