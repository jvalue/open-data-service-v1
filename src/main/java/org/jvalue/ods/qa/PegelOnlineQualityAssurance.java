package org.jvalue.ods.qa;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ValueType;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.QualityAssuranceMain;
import org.jvalue.ods.qa.valueTypes.Coordinate;
import org.jvalue.si.QuantityUnit;
import org.jvalue.si.SiUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PegelOnlineQualityAssurance {

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	public PegelOnlineQualityAssurance() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");

	}

	public void checkValueTypes() {

		try {

			List<JsonNode> nodes = null;
			ObjectMapper mapper = new ObjectMapper();

			try {
				dbAccessor.connect();

				nodes = dbAccessor.executeDocumentQuery("_design/pegelonline",
						"getAllStations", null);

			} catch (RuntimeException e) {
				String errorMessage = "Could not retrieve data from db: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
			}

			for (JsonNode n : nodes) {
				if (n.isObject()) {
					HashMap<String, JsonNode> doc;

					doc = mapper.readValue(n.toString(),
							new TypeReference<HashMap<String, JsonNode>>() {
							});

					JsonNode latitude = doc.get("latitude");
					JsonNode longitude = doc.get("longitude");

					if (latitude != null && longitude != null) {

						try {

							new Coordinate(Double.parseDouble(latitude
									.toString()), Double.parseDouble(longitude
									.toString()));
						} catch (IllegalArgumentException e) {
							String errmsg = "Invalid coordinate: " + latitude
									+ "," + longitude;
							System.err.println(errmsg);
							Logging.error(this.getClass(), errmsg);
						}
					} else {
						// no coordinates for this station
					}
					List<JsonNode> timeseries = mapper.readValue(
							doc.get("timeseries").toString(),
							new TypeReference<LinkedList<JsonNode>>() {
							});

					for (JsonNode n2 : timeseries) {
						if (n2.isObject()) {
							HashMap<String, JsonNode> doc2;
							doc2 = mapper
									.readValue(
											n2.toString(),
											new TypeReference<HashMap<String, JsonNode>>() {
											});

							HashMap<String, JsonNode> currentMeasurement = mapper
									.readValue(
											doc2.get("currentMeasurement")
													.toString(),
											new TypeReference<HashMap<String, JsonNode>>() {
											});

							JsonNode trend = currentMeasurement.get("trend");

							ValueType waterLevelTrendType = QualityAssuranceMain
									.getValueTypes().get("waterLevelTrend");

							boolean trendIsValid = waterLevelTrendType
									.isValidInstance(trend.toString());

							if (!trendIsValid) {
								String errmsg = "Invalid trend: " + trend;
								System.err.println(errmsg);
								Logging.error(this.getClass(), errmsg);
							}

							JsonNode unit = n2.get("unit");

							if (unit.toString().equals("cm")) {

								JsonNode value = currentMeasurement
										.get("value");

								ValueType qut = QualityAssuranceMain
										.getValueTypes().get("waterLevel");

								boolean valueIsValid = qut
										.isValidInstance(new QuantityUnit(value
												.asDouble() / 100.0, SiUnit.m));

								if (!valueIsValid) {
									String errmsg = "Negative water level: "
											+ value;
									System.err.println(errmsg);
									Logging.error(this.getClass(), errmsg);
								}
							}
						}
					}

				}
			}

		} catch (IOException e) {
			String errorMessage = "Error during client request: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}
	}

}
