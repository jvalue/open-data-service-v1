package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import java.io.IOException;

@RunWith(JMockit.class)
public final class DataTransformationManagerTest {
	private DataTransformationManager transformationManager;

	@Mocked
	private NashornExecutionEngine executionEngine;

	ObjectNode objectNode;
	@Before
	public void setupTransformationManager() {
		this.transformationManager = new DataTransformationManager(executionEngine);
		objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("key1", "test1");
		objectNode.put("key2", "test2");
	}


	@Test
	public void testTransform() throws ScriptException, IOException, NoSuchMethodException {
		TransformationFunction function = new TransformationFunction("1", "function");

		new Expectations() {{
			executionEngine.execute(null, function);
			result = objectNode;
		}};

		ObjectNode res = transformationManager.transform(null, function);
		Assert.assertEquals(objectNode, res);
	}
}
