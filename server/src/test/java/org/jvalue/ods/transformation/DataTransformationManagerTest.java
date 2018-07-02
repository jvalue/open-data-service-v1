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


	@Before
	public void setupTransformationManager() {
		this.transformationManager = new DataTransformationManager(executionEngine);
	}


	@Test
	public void testTransform() throws ScriptException, IOException {
		TransformationFunction function = new TransformationFunction("1", "function");

		new Expectations() {{
			executionEngine.execute(null, function);
			result = new ObjectMapper().createObjectNode();
		}};

		ObjectNode res = transformationManager.transform(null, function);
		Assert.assertEquals(new ObjectMapper().createObjectNode(), res);
	}
}
