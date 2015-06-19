package org.jvalue.ods.processor.filter.domain;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.filter.AbstractDataModifierFilter;
import org.jvalue.ods.processor.filter.FilterException;

import javax.inject.Inject;


/**
 * Prepares data received from water portal of the German state Brandenburg
 * to be merge with other water level * data sources.
 */
public class PegelBrandenburgMerger extends AbstractDataModifierFilter {

	@Inject
	PegelBrandenburgMerger(@Assisted DataSource source, MetricRegistry registry) {
		super(source, registry);
	}

	@Override
	protected ObjectNode doFilter(ObjectNode data) throws FilterException {
		// remove uuid (not needed)
		data.remove("uuid");

		// create current measurements object
		ObjectNode newMeasurements = data.putObject("currentMeasurement");
		newMeasurements.put("timestamp", data.path("measurementTime"));
		data.remove("measurementTime");

		String waterLevel = data.path("waterLevel").asText();
		data.remove("waterLevel");
		if (!waterLevel.equals("-")) {
			newMeasurements.put("value", waterLevel.split(" ")[0]);
			newMeasurements.put("unit", waterLevel.split(" ")[1]);
		}

		newMeasurements.putArray("characteristicValues");

		// remove flowrate
		data.remove("flowrate");

		// remove trend info
		data.remove("trend");
		data.remove("trendIcon");
		data.remove("trendTitle");

		// remove brackets from station name
		String stationName = data.get("name").asText();
		data.put("name", stationName.substring(0, stationName.indexOf('(')));

		// rename rivershed to water
		rename(data, "rivershed", "water");

		return data;
	}

	@Override
	protected void doOnComplete() throws FilterException {
		// nothing to do
	}

}
