package org.jvalue.ods.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.api.views.generic.TransformationFunction;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.transformation.ExecutionEngine;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

public class DataTransformationManager extends AbstractDataSourcePropertyManager<TransformationFunction, GenericRepository<TransformationFunction>> {
	private final ExecutionEngine executionEngine;


	@Inject
	public DataTransformationManager(ExecutionEngine executionEngine,
									 Cache<GenericRepository<TransformationFunction>> viewRepositoryCache,
									 RepositoryFactory repositoryFactory) {
		super(viewRepositoryCache, repositoryFactory);
		this.executionEngine = executionEngine;
	}


	public ObjectNode transform(ObjectNode data, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		return executionEngine.execute(data, transformationFunction);
	}


	public ArrayNode transform(GenericDataRepository<JsonNode> dataRepository, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		Data paginatedData = dataRepository.getPaginatedData(null, 100);
		List<JsonNode> result = paginatedData.getResult();

		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);

		for (JsonNode jsonNode : result){
			ObjectNode execute = executionEngine.execute(jsonNode.deepCopy(), transformationFunction);
			arrayNode.add(execute);
		}
		return arrayNode;
	}


	@Override
	protected void doAdd(DataSource source, GenericDataRepository<JsonNode> dataRepository, TransformationFunction data) {

	}


	@Override
	protected void doRemove(DataSource source, GenericDataRepository<JsonNode> dataRepository, TransformationFunction data) {

	}


	@Override
	protected void doRemoveAll(DataSource source) {

	}


	@Override
	protected GenericRepository<TransformationFunction> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createTransformationFunctionRepository(sourceId);
	}
}
