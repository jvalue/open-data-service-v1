package org.jvalue.ods.sources;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.processor.reference.ExecutionInterval;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.Processor;
import org.jvalue.ods.rest.model.ProcessorChainReference;

import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public final class PegelOnlineTest extends AbstractDataSourceTest {

	@Test
	public void testTrashCanSource() throws Exception {
		final DataSource source = new DataSource();
		source.url = new URL("http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true&includeCharacteristicValues=true&waters=ELBE");
		source.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
		source.domainIdKey = "/uuid";
		source.schema = new ObjectNode(JsonNodeFactory.instance);

		final ProcessorChainReference processorChain = new ProcessorChainReference();
		processorChain.processors = new LinkedList<>();
		processorChain.executionInterval = new ExecutionInterval(100, TimeUnit.SECONDS);

		final Processor adapterFilter = new Processor();
		adapterFilter.name = "JsonSourceAdapter";

		final Processor dbFilter = new Processor();
		dbFilter.name = "DbInsertionFilter";

		final Processor notificationFilter = new Processor();
		notificationFilter.name = "NotificationFilter";

		processorChain.processors.add(adapterFilter);
		processorChain.processors.add(dbFilter);
		processorChain.processors.add(notificationFilter);

		runTest(source, processorChain, 2000);
	}

}
