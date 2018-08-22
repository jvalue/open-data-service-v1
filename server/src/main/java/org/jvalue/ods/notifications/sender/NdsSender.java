package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.rabbitmq.client.BuiltinExchangeType;
import org.apache.commons.io.IOUtils;
import org.jvalue.ods.api.notifications.NdsClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.domain.weather.models.Temperature;
import org.jvalue.ods.processor.adapter.domain.weather.models.TemperatureType;
import org.jvalue.ods.processor.adapter.domain.weather.models.Weather;
import org.jvalue.ods.pubsub.Publisher;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;
import java.io.InputStream;

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
		boolean connected = publisher.connect(client.getHost(), client.getExchange(), EXCHANGE_TYPE);

		boolean sent = true;

		for (JsonNode node : buffer) {
			Weather weather = getWeatherFromJsonNode(node);
			String message = createCIMRepresentation(weather);
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


	private String createCIMRepresentation(Weather weather) {
		Temperature temperature = getTemperatureInCelsius(weather.getTemperature());
		String cimTemplate = getCimTemplate();

		return cimTemplate
			.replace("__ID__", weather.getStationId())
			.replace("__CITY__", weather.getLocation().getCity())
			.replace("__TEMP_IN_C__", String.valueOf(temperature.getValue()))
			.replace("__SOLAR_RADIATION__", "200.243")
			.replace("__TIMESTAMP__", weather.getTimestamp().toString());
	}


	private String createRoutingKey(String city) {
		return "EnvironmentalMeasurement." + city;
	}


	private Weather getWeatherFromJsonNode(JsonNode node) {
		JsonNode clearNode = removeCouchDbElements(node);
		return JsonMapper.convertValue(clearNode, Weather.class);
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
		InputStream resource = NdsSender.class.getClassLoader().getResourceAsStream("nds/CIM-Template.xml");
		try {
			return IOUtils.toString(resource);
		} catch (IOException e) {
			return "UNKNOWN";
		}
	}
}
