package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.views.generic.TransformationFunction;
import org.jvalue.ods.data.DataTransformationManager;
import org.jvalue.ods.db.generic.RepositoryFactory;

import javax.script.ScriptException;
import java.io.IOException;

@RunWith(JMockit.class)
public final class DataTransformationManagerTest {
	private DataTransformationManager transformationManager;

	@Mocked private RepositoryFactory repositoryFactory;
	@Mocked private Cache<GenericRepository<TransformationFunction>> dataRepositoryCache;
	@Mocked private NashornExecutionEngine executionEngine;

	private ObjectNode objectNode;

	@Before
	public void setupTransformationManager() {
		this.transformationManager = new DataTransformationManager(executionEngine, dataRepositoryCache, repositoryFactory);
		objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("key1", "test1");
		objectNode.put("key2", "test2");
	}


	@Test
	public void testTransform() throws ScriptException, IOException, NoSuchMethodException {
		TransformationFunction function = new TransformationFunction("1", "function");

		new Expectations() {{
			executionEngine.execute(new ObjectNode(JsonNodeFactory.instance), function);
			result = objectNode;
		}};

		ObjectNode res = transformationManager.transform(new ObjectNode(JsonNodeFactory.instance), function);
		Assert.assertEquals(objectNode, res);
	}
}
