package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.processor.reference.ProcessorChainReference;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.processorChainId) emit(null, doc) }")
public final class ProcessorChainReferenceRepository extends CouchDbRepositorySupport<ProcessorChainReference> {

	@Inject
	ProcessorChainReferenceRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(ProcessorChainReference.class, couchDbInstance.createConnector(databaseName, true));
		initStandardDesignDocument();
	}


	@GenerateView
	public ProcessorChainReference findByProcessorChainId(String processorChainId) {
		List<ProcessorChainReference> chains = queryView("by_processorChainId", processorChainId);
		if (chains.isEmpty()) throw new DocumentNotFoundException(processorChainId);
		if (chains.size() > 1) throw new IllegalStateException("found more than one chain for id " + processorChainId);
		return chains.get(0);
	}

}
