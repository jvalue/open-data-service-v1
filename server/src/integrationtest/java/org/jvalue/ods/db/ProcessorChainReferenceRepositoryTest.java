package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ProcessorChainReferenceRepositoryTest extends AbstractRepositoryAdapterTest<ProcessorReferenceChain> {

	public ProcessorChainReferenceRepositoryTest() {
		super(ProcessorChainReferenceRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, ProcessorReferenceChain> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new ProcessorChainReferenceRepository(couchDbInstance, databaseName);
	}


	@Override
	protected ProcessorReferenceChain doCreateValue(String id, String data) {
		return new ProcessorReferenceChain(
				id,
				Arrays.asList(new ProcessorReference.Builder(data).build()),
				new ExecutionInterval(0, TimeUnit.SECONDS));
	}

}
