package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

import javax.inject.Inject;

public class MongoDbProcessorChainReferenceRepository extends MongoDbRepositoryAdapter<
	MongoDbProcessorChainReferenceRepository.MongoDbProcessorChainReferenceRepositoryImpl,
	MongoDbProcessorChainReferenceRepository.MongoDbProcessorChainReferenceDocument,
	ProcessorReferenceChain> implements GenericRepository<ProcessorReferenceChain> {

	private static final String COLLECTION_NAME = "processorChainReferenceCollection";


	@Inject
	public MongoDbProcessorChainReferenceRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new MongoDbProcessorChainReferenceRepositoryImpl(dbConnectorFactory, databaseName, COLLECTION_NAME));
	}

	static class MongoDbProcessorChainReferenceRepositoryImpl extends AbstractMongoDbRepository<MongoDbProcessorChainReferenceDocument> implements MongoDbDocumentAdaptable<MongoDbProcessorChainReferenceDocument, ProcessorReferenceChain> {

		protected MongoDbProcessorChainReferenceRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbProcessorChainReferenceDocument.class);
		}


		@Override
		protected MongoDbProcessorChainReferenceDocument createNewDocument(Document document) {
			return new MongoDbProcessorChainReferenceDocument(document);
		}


		@Override
		protected String getValueId(MongoDbProcessorChainReferenceDocument Value) {
			return Value.getValue().getId();
		}


		@Override
		public MongoDbProcessorChainReferenceDocument createDbDocument(ProcessorReferenceChain value) {
			return new MongoDbProcessorChainReferenceDocument(value);
		}


		@Override
		public String getIdForValue(ProcessorReferenceChain value) {
			return value.getId();
		}
	}

	static class MongoDbProcessorChainReferenceDocument extends MongoDbDocument<ProcessorReferenceChain> {
		public MongoDbProcessorChainReferenceDocument(ProcessorReferenceChain valueObject) {
			super(valueObject, ProcessorReferenceChain.class);
		}
		public MongoDbProcessorChainReferenceDocument(Document document) {
			super(document, ProcessorReferenceChain.class);
		}
	}
}
