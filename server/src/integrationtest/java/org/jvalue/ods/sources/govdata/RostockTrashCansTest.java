package org.jvalue.ods.sources.govdata;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.sources.AbstractDataSourceTest;

import java.util.concurrent.TimeUnit;

public final class RostockTrashCansTest extends AbstractDataSourceTest {

	@Override
	public DataSourceDescription getSourceDescription() {
		return new DataSourceDescription(
				JsonPointer.compile("/uuid"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}


	@Override
	public String getStartId() {
		return "20cbcdf6-103a-11e5-916a-0050569b7e95";
	}


	@Override
	public ProcessorReferenceChainDescription getProcessorChainDescription() {
		return new ProcessorReferenceChainDescription.Builder(
				new ExecutionInterval(100, TimeUnit.SECONDS))
				.processor(new ProcessorReference.Builder("CsvSourceAdapter")
						.argument("sourceUrl", "https://geo.sv.rostock.de/download/opendata/abfallbehaelter/abfallbehaelter.csv")
						.argument("csvFormat", "DEFAULT")
						.build())
				.processor(new ProcessorReference.Builder("CouchDbInsertionFilter")
						.argument("updateData", true)
						.build())
				.build();
	}


	@Override
	public long getSleepDuration() {
		return 3000;
	}

}
