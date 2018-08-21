package org.jvalue.ods.db.couchdb;


import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.db.couchdb.repositories.ProcessorChainReferenceRepository;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ProcessorChainReferenceRepositoryTest extends AbstractRepositoryAdapterTest<ProcessorReferenceChain> {

	@Override
	protected RepositoryAdapter<?, ?, ProcessorReferenceChain> doCreateAdapter(DbConnectorFactory dbConnectorFactory) {
		return new ProcessorChainReferenceRepository(dbConnectorFactory, getClass().getSimpleName());
	}


	@Override
	protected ProcessorReferenceChain doCreateValue(String id, String data) {
		return new ProcessorReferenceChain(
				id,
				Arrays.asList(new ProcessorReference.Builder(data).build()),
				new ExecutionInterval(0, TimeUnit.SECONDS));
	}

}
