package org.jvalue.ods.processor.filter.domain;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.filter.AbstractDataModifierFilter;
import org.jvalue.ods.processor.filter.FilterException;

import javax.inject.Inject;


/**
 * Prepares data received from PegelOnline to be merge with other water level
 * data sources.
 */
public class PegelOnlineMerger extends AbstractDataModifierFilter {

	@Inject
	PegelOnlineMerger(@Assisted DataSource source, MetricRegistry registry) {
		super(source, registry);
	}

	@Override
	protected ObjectNode doFilter(ObjectNode data) throws FilterException {
		// remove uuid (not needed)
		data.remove("uuid");

		// rename number to gaugeId
		rename(data, "number", "gaugeId");

		// move measurement "up"
		ObjectNode oldMeasurements = null;
		for (JsonNode timeseriesNode : data.get("timeseries")) {
			if (timeseriesNode.path("shortname").asText().equals("W")) {
				oldMeasurements = (ObjectNode) timeseriesNode;
				break;
			}
		}
		if (oldMeasurements != null) {
			ObjectNode newMeasurements = data.putObject("currentMeasurement");
			newMeasurements.put("unit", oldMeasurements.path("unit").asText());
			newMeasurements.put("timestamp", oldMeasurements.path("currentMeasurement").path("timestamp"));
			newMeasurements.put("value", oldMeasurements.path("currentMeasurement").path("value"));
			newMeasurements.put("characteristicValues", oldMeasurements.path("characteristicValues"));
		}
		data.remove("timeseries");

		return data;
	}

	@Override
	protected void doOnComplete() throws FilterException {
		// nothing to do
	}

}
