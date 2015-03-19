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

public final class CamperParkingBremenTest extends AbstractDataSourceTest {

	@Override
	public DataSourceDescription getSourceDescription() {
		return new DataSourceDescription(
				JsonPointer.compile("/id"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
	}


	@Override
	public String getStartId() {
		return "2245803";
	}


	@Override
	public ProcessorReferenceChainDescription getProcessorChainDescription() {
		return new ProcessorReferenceChainDescription.Builder(
				new ExecutionInterval(100, TimeUnit.SECONDS))
				.processor(new ProcessorReference.Builder("XmlSourceAdapter")
						.argument("sourceUrl", "http://www.bremen.de/sixcms/detail.php?template=export_vk_d&kat=Wohnmobilstellpl%C3%A4tze")
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
