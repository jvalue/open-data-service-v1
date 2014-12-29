package org.jvalue.ods.sources.govdata;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.processor.reference.ExecutionInterval;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.Processor;
import org.jvalue.ods.rest.model.ProcessorChainReference;
import org.jvalue.ods.sources.AbstractDataSourceTest;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public final class CamperParkingBremen extends AbstractDataSourceTest {

	@Test
	public void testTrashCanSource() throws Exception {
		final DataSource source = new DataSource();
		source.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
		source.domainIdKey = "/id";
		source.schema = new ObjectNode(JsonNodeFactory.instance);

		final ProcessorChainReference processorChain = new ProcessorChainReference();
		processorChain.processors = new LinkedList<>();
		processorChain.executionInterval = new ExecutionInterval(100, TimeUnit.SECONDS);

		final Processor adapterFilter = new Processor();
		adapterFilter.name = "XmlSourceAdapter";
		adapterFilter.arguments.put("sourceUrl", "http://www.bremen.de/sixcms/detail.php?template=export_vk_d&kat=Wohnmobilstellpl%C3%A4tze");

		final Processor docFilter = new Processor();
		docFilter.name = "InvalidDocumentFilter";

		final Processor dbFilter = new Processor();
		dbFilter.name = "DbInsertionFilter";

		processorChain.processors.add(adapterFilter);
		processorChain.processors.add(docFilter);
		processorChain.processors.add(dbFilter);

		runTest(source, processorChain, 3000);
	}

}
