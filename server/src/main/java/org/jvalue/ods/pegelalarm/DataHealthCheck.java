package org.jvalue.ods.pegelalarm;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.data.DataSourceManager;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Asserts that for a set of 5 measuring stations that the average update timestamp is not older than 24 hours.
 */
public class DataHealthCheck extends HealthCheck implements Constants {

	private static final Set<String> stationIds = Sets.newHashSet("10046105", "10062000", "24300304", "26100100", "26400550");
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	private final DataSourceManager dataSourceManager;

	@Inject
	DataHealthCheck(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}


	@Override
	protected Result check() throws Exception {
		final DataSource source = dataSourceManager.findBySourceId(DATA_SOURCE_ID);
		final GenericDataRepository<CouchDbDataView, JsonNode> dataRepository = dataSourceManager.getDataRepository(source);

		long averageTimestamp = 0;

		for (final String stationId : stationIds) {
			final JsonNode data = dataRepository.findByDomainId(stationId);
			if (data == null) return Result.unhealthy("on station with id " + stationId);
			averageTimestamp += dateFormat.parse(data.get("currentMeasurement").get("timestamp").asText()).getTime();
		}

		averageTimestamp /= stationIds.size();
		long yesterday = System.currentTimeMillis() - 86400000; // minus 1 day in ms

		if (averageTimestamp <= yesterday) return Result.unhealthy("average timestamp is older than 24 hours");
		return Result.healthy();
	}
}
