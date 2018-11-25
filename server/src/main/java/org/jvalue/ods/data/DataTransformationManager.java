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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataTransformationManager extends AbstractDataSourcePropertyManager<TransformationFunction, GenericRepository<TransformationFunction>> {
	private final ExecutionEngine executionEngine;


	@Inject
	public DataTransformationManager(ExecutionEngine executionEngine,
									 Cache<GenericRepository<TransformationFunction>> viewRepositoryCache,
									 RepositoryFactory repositoryFactory) {
		super(viewRepositoryCache, repositoryFactory);
		this.executionEngine = executionEngine;
	}


	public ArrayNode transform(ObjectNode data, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		return executionEngine.execute(data, transformationFunction, false);
	}


	public ArrayNode transformAndReduce(GenericDataRepository<JsonNode> dataRepository, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		Data paginatedData = dataRepository.getAllDocuments();
		List<JsonNode> result = paginatedData.getResult();

		ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

		for (JsonNode jsonNode : result){
			ArrayNode resultSet = executionEngine.execute(jsonNode.deepCopy(), transformationFunction, true);
			Iterator<JsonNode> elements = resultSet.elements();
			while(elements.hasNext()){
				JsonNode next = elements.next();
				resultNode.add(next);
			}
		}

		if(transformationFunction.getReduceFunction() != null)
			return executionEngine.reduce(resultNode.deepCopy(), transformationFunction);

		return resultNode;
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
