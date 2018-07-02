package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.transformation.DataTransformationManager;
import org.jvalue.ods.transformation.TransformationFunction;

import javax.script.ScriptException;
import java.io.IOException;

public class TransformationFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	private final String transformationFunction;
	private final DataTransformationManager dataTransformationManager;


	@Inject
	public TransformationFilter(
			@Assisted DataSource source,
			MetricRegistry registry,
			DataTransformationManager dataTransformationManager,
			String transformationFunction) {
		super(source, registry);
		this.transformationFunction = transformationFunction;
		this.dataTransformationManager = dataTransformationManager;
	}


	@Override
	protected ObjectNode doFilter(ObjectNode node) throws FilterException {
		ObjectNode result;
		try {
			 result = dataTransformationManager.transform(node, new TransformationFunction(source.getId(), transformationFunction));
		} catch (ScriptException | IOException e) {
			throw new FilterException(e.getMessage(), e);
		}

		return result;
	}


	@Override
	protected void doOnComplete() throws FilterException {

	}
}
