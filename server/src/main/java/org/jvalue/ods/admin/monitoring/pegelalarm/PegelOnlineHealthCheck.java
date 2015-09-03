package org.jvalue.ods.admin.monitoring.pegelalarm;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;

/**
 * Asserts that http://pegelonline.wsv.de is reachable and contains valid JSON.
 */
public class PegelOnlineHealthCheck extends HealthCheck {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected Result check() throws Exception {
		JsonNode json = mapper.readTree(new URL("http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json").openStream());
		if (!json.isArray()) return Result.unhealthy("pegelonline did not return JSON array");
		if (json.size() == 0) return Result.unhealthy("pegelonline returned empty JSON");
		return Result.healthy();
	}

}
