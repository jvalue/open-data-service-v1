package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataTransformationManager;
import org.jvalue.ods.api.views.generic.TransformationFunction;

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
		ArrayNode result;
		try {
			result = dataTransformationManager.transform(node, new TransformationFunction(source.getId(), transformationFunction), false);
		} catch (Exception e) {
			// nothing to do if exception occurs -> throw it.
			throw new FilterException(e.getMessage(), e);
		}

		return (ObjectNode) result.get(0);
	}


	@Override
	protected void doOnComplete() throws FilterException {
	}
}
