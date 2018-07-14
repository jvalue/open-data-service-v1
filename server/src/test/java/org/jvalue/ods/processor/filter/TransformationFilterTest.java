package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.transformation.DataTransformationManager;
import org.jvalue.ods.transformation.ExecutionEngine;
import org.jvalue.ods.transformation.NashornExecutionEngine;

@RunWith(JMockit.class)
public final class TransformationFilterTest {

	@Mocked
	private MetricRegistry registry;

	private DataSource source;
	private ObjectNode baseNode;

	private DataTransformationManager transformationManager;


	private static final String extension =
		"function transform(doc){"
			+ "    if(doc != null){"
			+ "        doc.extension = \"This is an extension\";"
			+ "    }"
			+ "    return doc;"
			+ "};";

	private static final String fail =
		"function test(doc){};";


	@Before
	public void setUp() {
		this.source = createDataSource("/parent/id");
		this.baseNode = new ObjectNode(JsonNodeFactory.instance);

		ObjectNode innerNode = new ObjectNode(JsonNodeFactory.instance);
		innerNode.put("temp", 0);
		baseNode.set("main", innerNode);
		baseNode.put("key2", "value1");

		ExecutionEngine executionEngine = new NashornExecutionEngine();
		this.transformationManager = new DataTransformationManager(executionEngine);
	}


	@Test
	public void testTransformation() throws Exception {
		ObjectNode resultNode = applyFilter(extension);
		Assert.assertTrue(resultNode.has("extension"));
	}


	@Test(expected = FilterException.class)
	public void testFailTransformation() throws Exception {
		applyFilter(fail);
	}


	private DataSource createDataSource(String jsonPointer) {
		return new DataSource(
			"someId",
			JsonPointer.compile(jsonPointer),
			new ObjectNode(JsonNodeFactory.instance),
			new DataSourceMetaData("", "", "", "", "", "", ""));
	}


	private ObjectNode applyFilter(String transformationFunction) throws Exception {
		return new TransformationFilter(source, registry, transformationManager, transformationFunction).doFilter(baseNode);
	}
}
