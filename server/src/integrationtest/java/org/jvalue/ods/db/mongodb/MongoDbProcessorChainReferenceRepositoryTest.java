package org.jvalue.ods.db.mongodb;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.mongodb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.db.mongodb.repositories.MongoDbProcessorChainReferenceRepository;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Ignore
public class MongoDbProcessorChainReferenceRepositoryTest extends AbstractRepositoryAdapterTest<ProcessorReferenceChain> {
	@Override
	protected GenericRepository<ProcessorReferenceChain> doCreateRepository(DbConnectorFactory connectorFactory) {
		return new MongoDbProcessorChainReferenceRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected ProcessorReferenceChain doCreateValue(String id, String data) {
		return new ProcessorReferenceChain(
			id,
			Arrays.asList(new ProcessorReference.Builder(data).build()),
			new ExecutionInterval(0, TimeUnit.SECONDS));
	}


	@Override
	protected void doDeleteDatabase(DbConnectorFactory dbConnectorFactory) {
		dbConnectorFactory.doDeleteDatabase(getClass().getSimpleName());
	}
}
