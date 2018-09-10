package org.jvalue.ods.processor;


import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.FilterFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class ProcessorChainFactoryTest {

	private static final String simpleExtension =
			"function transform(doc){"
					+ "    if(doc != null){"
					+ "        doc.extension = \"This is an extension\";"
					+ "    }"
					+ "    return doc;"
					+ "};";


	private final ProcessorReferenceChain chainReference;
	{
		Map<String, Object> dbFilterArgs = new HashMap<>();
		dbFilterArgs.put("updateData", true);

		Map<String, Object> transformationFilterArgs = new HashMap<>();
		transformationFilterArgs.put("transformationFunction",simpleExtension);

		List<ProcessorReference> references = new LinkedList<>();
		references.add(new ProcessorReference(SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER, new HashMap<String, Object>()));
		references.add(new ProcessorReference(FilterFactory.NAME_DB_INSERTION_FILTER, dbFilterArgs));
		references.add(new ProcessorReference(FilterFactory.NAME_NOTIFICATION_FILTER, new HashMap<String, Object>()));
		references.add(new ProcessorReference(FilterFactory.NAME_TRANSFORMATION_FILTER, transformationFilterArgs));

		chainReference = new ProcessorReferenceChain("someId", references, new ExecutionInterval(12, TimeUnit.MILLISECONDS));
	}


	@Test
	public void testCreation(
			@Mocked final FilterFactory filterFactory,
			@Mocked final SourceAdapterFactory adapterFactory,
			@Mocked final GenericDataRepository<JsonNode> dataRepository)
			throws Exception {

		final ProcessorChainFactory chainFactory = new ProcessorChainFactory(adapterFactory, filterFactory);
		chainFactory.createProcessorChain(chainReference, new DataSource(null, null, null, null), dataRepository);

		new Verifications() {{
			adapterFactory.createJsonSourceAdapter((DataSource) any, anyString); times = 1;
			filterFactory.createDbInsertionFilter((DataSource) any, (GenericDataRepository<JsonNode>) any, anyBoolean); times = 1;
			filterFactory.createNotificationFilter((DataSource) any); times = 1;
			filterFactory.createTransformationFilter((DataSource) any, simpleExtension); times = 1;
		}};
	}

}
