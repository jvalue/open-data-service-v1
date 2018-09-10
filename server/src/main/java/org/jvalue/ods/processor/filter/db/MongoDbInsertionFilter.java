package org.jvalue.ods.processor.filter.db;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.DocumentOperationResult;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.processor.filter.FilterException;

import java.util.Collection;
import java.util.List;

public class MongoDbInsertionFilter extends CouchDbInsertionFilter {


	@Inject
	MongoDbInsertionFilter(@Assisted GenericDataRepository<JsonNode> dataRepository,
						   @Assisted DataSource source,
						   @Assisted boolean updateDataIfExists,
						   MetricRegistry registry) {
		super(dataRepository, source, updateDataIfExists, registry);
	}

	@Override
	@SuppressWarnings({"unchecked", "Duplicates"})
	protected void writeBulkData() throws FilterException {
		//for now the mongodb insertion filter always updates the data if a new data set with same id is available
		timerContextBulkRead.resume();
		timerContextBulkRead.pause();
		timerContextBulkWrite.resume();
		try {
			Collection<DocumentOperationResult> results = dataRepository.writeBulk((List) bulkObjects);
			for (DocumentOperationResult result : results) {
				if (result.isErroneous())
					throw new FilterException("db insertion failed for id " + result.getId() + ", reason: " + result.getReason());
			}
		} finally {
			timerContextBulkWrite.pause();
		}

		bulkObjects.clear();
	}
}
