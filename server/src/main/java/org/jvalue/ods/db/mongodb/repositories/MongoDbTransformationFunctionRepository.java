package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.generic.TransformationFunction;
import org.value.commons.mongodb.AbstractMongoDbRepository;

public class MongoDbTransformationFunctionRepository extends AbstractMongoDbRepository<TransformationFunction> {

	private static final String COLLECTION_NAME = "transformationFunctionCollection";

	@Inject
	protected MongoDbTransformationFunctionRepository(DbConnectorFactory connectorFactory, @Assisted String databaseName) {
		super(connectorFactory, databaseName, COLLECTION_NAME, TransformationFunction.class);
	}


	@Override
	protected String getValueId(TransformationFunction Value) {
		return Value.getId();
	}
}
