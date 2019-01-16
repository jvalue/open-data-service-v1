/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter.domain;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.filter.AbstractDataModifierFilter;
import org.jvalue.ods.processor.filter.FilterException;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;


/**
 * Prepares data received from water portal of the German state Brandenburg
 * to be merge with other water level * data sources.
 */
public class PegelBrandenburgMerger extends AbstractDataModifierFilter {

	// removes stations which are also present in PegelOnline as PegelOnline bas better quality in general
	private static final Set<String> BLACKLISTED_STATIONS = Sets.newHashSet("9690093", "9690088", "580412");

	private static final DateFormat
			sourceDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm"),
			targetDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");


	@Inject
	PegelBrandenburgMerger(@Assisted DataSource source, MetricRegistry registry) {
		super(source, registry);
	}

	@Override
	protected ObjectNode doFilter(ObjectNode data) throws FilterException {
		// remove duplicate stations
		if (BLACKLISTED_STATIONS.contains(data.path("gaugeId").asText())) {
			Log.info("Dropped station with id " + data.path("gaugeId").asText());
			return null;
		}

		// remove uuid (not needed)
		data.remove("uuid");

		// create current measurements object
		ObjectNode newMeasurements = data.putObject("currentMeasurement");
		newMeasurements.put("timestamp", data.path("measurementTime"));
		data.remove("measurementTime");

		String waterLevel = data.path("waterLevel").asText();
		data.remove("waterLevel");
		if (!waterLevel.equals("-")) {
			String value = waterLevel.split(" ")[0];
			value = value.replaceAll(",", ".");
			newMeasurements.put("value", Double.valueOf(value));
			newMeasurements.put("unit", waterLevel.split(" ")[1]);
		}

		newMeasurements.putArray("characteristicValues");

		// reformat timestamp
		String timestamp = newMeasurements.path("timestamp").asText();
		if (timestamp.equals("-")) {
			newMeasurements.remove("timestamp");
		}
		else {
			try {
				newMeasurements.put("timestamp", targetDateFormat.format(sourceDateFormat.parse(timestamp)));
			} catch (ParseException p) {
				Log.warn("failed to parse timestamp " + timestamp);
				newMeasurements.remove("timestamp");
			}
		}

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


	private long parseStringTimestamp(String stringTimestamp) {
		Assert.assertNotNull(stringTimestamp);
		try {
			return targetDateFormat.parse(stringTimestamp).getTime();
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}

}
