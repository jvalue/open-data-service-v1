package org.jvalue.ods.sources;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.concurrent.TimeUnit;

public final class PegelOnlineTest extends AbstractDataSourceTest {

	@Override
	public DataSourceDescription getSourceDescription() {
		return new DataSourceDescription(
				JsonPointer.compile("/uuid"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}


	@Override
	public String getStartId() {
		return "47174d8f-1b8e-4599-8a59-b580dd55bc87";
	}


	@Override
	public ProcessorReferenceChainDescription getProcessorChainDescription() {
		return new ProcessorReferenceChainDescription.Builder(
				new ExecutionInterval(100, TimeUnit.SECONDS))
				.processor(new ProcessorReference.Builder("JsonSourceAdapter")
						.argument("sourceUrl", "http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true&includeCharacteristicValues=true&waters=ELBE")
						.build())
				.processor(new ProcessorReference.Builder("DbInsertionFilter")
						.argument("updateData", true)
						.build())
				.build();
	}


	@Override
	public long getSleepDuration() {
		return 2000;
	}

}
