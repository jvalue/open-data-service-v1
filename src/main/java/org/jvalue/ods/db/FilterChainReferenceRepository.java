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
	public List<FilterChainReference> findByFilterChainId(String filterChainId) {
		return queryView("by_filterChainId", filterChainId);
	}


	public FilterChainReference findByFilterChainAndSourceId(String dataSourceId, String filterChainId) {
		List<FilterChainReference> references = findByDataSourceId(dataSourceId);
		for (FilterChainReference reference : references)
			if (reference.getFilterChainId().equals(filterChainId)) return reference;
		throw new DocumentNotFoundException(dataSourceId + "/" + filterChainId);
	}

}
