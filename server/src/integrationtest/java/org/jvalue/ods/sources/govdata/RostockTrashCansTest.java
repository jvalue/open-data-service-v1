package org.jvalue.ods.sources.govdata;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.Processor;
import org.jvalue.ods.api.processors.ProcessorChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.sources.AbstractDataSourceTest;

import java.util.concurrent.TimeUnit;

public final class RostockTrashCansTest extends AbstractDataSourceTest {

	@Test
	public void testTrashCanSource() throws Exception {
		final DataSourceDescription sourceDescription = new DataSourceDescription(
				JsonPointer.compile("/id"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));

		final ProcessorChainDescription processorChainDescription = new ProcessorChainDescription.Builder(
				new ExecutionInterval(100, TimeUnit.SECONDS))
				.processor(new Processor.Builder("CsvSourceAdapter")
						.argument("sourceUrl", "https://geo.sv.rostock.de/download/opendata/abfallbehaelter/abfallbehaelter.csv")
						.argument("csvFormat", "DEFAULT")
						.build())
				.processor(new Processor.Builder("DbInsertionFilter")
						.argument("updateData", true)
						.build())
				.build();

		runTest(sourceDescription, processorChainDescription, 3000);
	}

}
