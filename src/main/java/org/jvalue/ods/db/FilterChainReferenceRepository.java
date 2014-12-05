package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.filter.reference.FilterChainReference;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.filterChainId) emit(null, doc) }")
public final class FilterChainReferenceRepository extends CouchDbRepositorySupport<FilterChainReference> {

	@Inject
	FilterChainReferenceRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(FilterChainReference.class, couchDbInstance.createConnector(databaseName, true));
		initStandardDesignDocument();
	}


	@GenerateView
	public FilterChainReference findByFilterChainId(String filterChainId) {
		List<FilterChainReference> chains = queryView("by_filterChainId", filterChainId);
		if (chains.isEmpty()) throw new DocumentNotFoundException(filterChainId);
		if (chains.size() > 1) throw new IllegalStateException("found more than one filter chain for id " + filterChainId);
		return chains.get(0);
	}

}
