package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.db.ProcessorChainReferenceRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.util.List;

public class CouchDbProcessorChainReferenceRepositoryWrapper implements IRepository<ProcessorReferenceChain>{

	private ProcessorChainReferenceRepository processorChainReferenceRepository;

	@Inject
	CouchDbProcessorChainReferenceRepositoryWrapper(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		this.processorChainReferenceRepository = new ProcessorChainReferenceRepository(dbConnectorFactory, databaseName);
	}


	@Override
	public ProcessorReferenceChain findById(String Id) {
		return processorChainReferenceRepository.findById(Id);
	}


	@Override
	public void add(ProcessorReferenceChain Value) {
		processorChainReferenceRepository.add(Value);
	}


	@Override
	public void update(ProcessorReferenceChain value) {
		processorChainReferenceRepository.update(value);
	}


	@Override
	public void remove(ProcessorReferenceChain Value) {
		processorChainReferenceRepository.remove(Value);
	}


	@Override
	public List<ProcessorReferenceChain> getAll() {
		return processorChainReferenceRepository.getAll();
	}
}
