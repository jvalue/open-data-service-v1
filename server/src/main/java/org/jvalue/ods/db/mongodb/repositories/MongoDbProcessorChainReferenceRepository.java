package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import javax.inject.Inject;

public class MongoDbProcessorChainReferenceRepository extends AbstractMongoDbRepository<ProcessorReferenceChain> {

	private static final String COLLECTION_NAME = "processorChainReferenceCollection";


	@Inject
	public MongoDbProcessorChainReferenceRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, ProcessorReferenceChain.class);
	}
}
