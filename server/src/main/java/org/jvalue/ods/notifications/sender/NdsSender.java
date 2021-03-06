/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.rabbitmq.client.BuiltinExchangeType;
import org.apache.commons.io.IOUtils;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.notifications.NdsClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.domain.weather.models.Temperature;
import org.jvalue.ods.processor.adapter.domain.weather.models.TemperatureType;
import org.jvalue.ods.processor.adapter.domain.weather.models.extended.ExtendedWeather;
import org.jvalue.ods.pubsub.Publisher;
import org.jvalue.ods.utils.JsonMapper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.UUID;

@SuppressWarnings("Duplicates")
public class NdsSender extends AbstractSender<NdsClient> {

	private final static String EXCHANGE_TYPE = BuiltinExchangeType.TOPIC.getType();
	private final ArrayNode buffer = new ArrayNode(JsonNodeFactory.instance);
	private final Publisher publisher;

	@Inject
	protected NdsSender(@Assisted DataSource source, @Assisted NdsClient client, Publisher publisher) {
		super(source, client);
		this.publisher = publisher;
	}


	@Override
	public void onNewDataStart() {
		// nothing to do here
	}


	@Override
	public void onNewDataItem(ObjectNode data) {
		buffer.add(data);
	}


	@Override
	public void onNewDataComplete() {
		boolean connected = publisher.connect(client.getUri(), client.getExchange(), EXCHANGE_TYPE);

		boolean sent = true;

		for (JsonNode node : buffer) {
			ExtendedWeather weather = getWeatherFromJsonNode(node);
			String message = createCIMRepresentation(weather);

			if (client.getValidateMessage()) {
				validateMessage(message);
			}

			String routingKey = createRoutingKey(weather.getLocation().getCity());

			boolean nodeSent = publisher.publish(message, routingKey);
			if (!nodeSent) sent = false;
		}

		publisher.close();

		if (!connected) {
			setErrorResult("Unable to connect to publish/subscribe server.");
		} else if (!sent) {
			setErrorResult("Unable to send message to publish/subscribe server.");
		} else {
			setSuccessResult();
		}
	}


	private String createCIMRepresentation(ExtendedWeather weather) {
		Temperature temperature = getTemperatureInCelsius(weather.getTemperature());
		String cimTemplate = getCimTemplate();

		cimTemplate = cimTemplate
			.replace("__UUID4__", UUID.randomUUID().toString())
			.replace("__CITY__", weather.getLocation().getCity())
			.replace("__TEMP_IN_C__", String.valueOf(temperature.getValue()))
			.replace("__TIMESTAMP__", weather.getTimestamp().toString());

		if (weather.getTotalSolarRadiation() != null) {
			double wattM2 = weather.getTotalSolarRadiation().toWattPerHourPerSquareMeter();
			cimTemplate = cimTemplate.replace("__SOLAR_RADIATION__", String.valueOf(wattM2));
		} else {
			String regex = "<cim:EnvironmentalAnalog>[\\s]*<cim:kind>irradianceGlobalHorizontal[\\s\\S]*?</cim:EnvironmentalAnalog>";
			cimTemplate = cimTemplate.replaceAll(regex, "");
		}

		return cimTemplate;
	}


	private String createRoutingKey(String city) {
		return "1.0.0.EnvironmentalMeasurement." + city;
	}


	private ExtendedWeather getWeatherFromJsonNode(JsonNode node) {
		JsonNode clearNode = removeCouchDbElements(node);
		return JsonMapper.convertValue(clearNode, ExtendedWeather.class);
	}


	private JsonNode removeCouchDbElements(JsonNode node) {
		ObjectNode objectNode = (ObjectNode) node;
		objectNode.remove("_id");
		objectNode.remove("_rev");
		return node;
	}


	private Temperature getTemperatureInCelsius(Temperature temperature) {
		if (!temperature.getType().equals(TemperatureType.CELSIUS)) {
			return Temperature.fromKelvin(temperature.getValueInKelvin(), TemperatureType.CELSIUS);
		} else {
			return temperature;
		}
	}


	private String getCimTemplate() {
		return doGetResource("nds/CIM-Template.xml");
	}


	private String getCimXmlSchema() {
		return doGetResource("nds/Weather.xsd");
	}


	private String doGetResource(String path) {
		InputStream resource = NdsSender.class.getClassLoader().getResourceAsStream(path);
		try {
			return IOUtils.toString(resource);
		} catch (IOException e) {
			Log.error("Unable to read resource " + path);
			return "UNKNOWN";
		}
	}


	private void validateMessage(String message) {
		if (!isValidXMLSchema(message, getCimXmlSchema())) {
			Log.warn("CIM weather message is not valid!");
		}
	}


	private boolean isValidXMLSchema(String xml, String xsd) {
		try {
			SchemaFactory factory =
				SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(new StringReader(xsd)));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xml)));
		} catch (IOException | SAXException e) {
			Log.warn(e.getMessage());
			return false;
		}
		return true;
	}
}
