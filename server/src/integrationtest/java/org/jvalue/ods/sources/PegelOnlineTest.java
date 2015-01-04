package org.jvalue.ods.sources;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.Processor;
import org.jvalue.ods.api.processors.ProcessorChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.concurrent.TimeUnit;

public final class PegelOnlineTest extends AbstractDataSourceTest {

	@Test
	public void testPegelOnlineSource() throws Exception {
		final DataSourceDescription sourceDescription = new DataSourceDescription(
				JsonPointer.compile("/uuid"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));

		final ProcessorChainDescription processorChainDescription = new ProcessorChainDescription.Builder(
				new ExecutionInterval(100, TimeUnit.SECONDS))
				.processor(new Processor.Builder("JsonSourceAdapter")
						.argument("sourceUrl", "http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true&includeCharacteristicValues=true&waters=ELBE")
						.build())
				.processor(new Processor.Builder("DbInsertionFilter")
						.argument("updateData", true)
						.build())
				.build();

		runTest(sourceDescription, processorChainDescription, 2000);
	}

}
