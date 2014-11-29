package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.filter.reference.FilterChainReference;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.filterReferences) emit(null, doc) }")
public final class FilterChainReferenceRepository extends CouchDbRepositorySupport<FilterChainReference> {

	@Inject
	FilterChainReferenceRepository(@Named(DataSourceRepository.DATABASE_NAME) CouchDbConnector connector) {
		super(FilterChainReference.class, connector);
		initStandardDesignDocument();
	}


	@GenerateView
	public List<FilterChainReference> findByDataSourceId(String dataSourceId) {
		return queryView("by_dataSourceId", dataSourceId);
	}


	@GenerateView
	public FilterChainReference findByFilterChainId(String filterChainId) {
		List<FilterChainReference> chains = queryView("by_filterChainId", filterChainId);
		if (chains.isEmpty()) throw new DocumentNotFoundException(filterChainId);
		if (chains.size() > 1) throw new IllegalStateException("found more than one filter chain for filterChainId " + filterChainId);
		return chains.get(0);
	}

}
