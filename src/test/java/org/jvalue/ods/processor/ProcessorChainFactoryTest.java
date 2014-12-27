package org.jvalue.ods.processor;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.filter.FilterFactory;
import org.jvalue.ods.processor.reference.ProcessorChainReference;
import org.jvalue.ods.processor.reference.ProcessorReference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static mockit.Deencapsulation.newInstance;

@RunWith(JMockit.class)
public final class ProcessorChainFactoryTest {

	@Test
	public void testCreation(
			@Mocked final FilterFactory filterFactory,
			@Mocked final SourceAdapterFactory adapterFactory,
			@Mocked final DataSource dataSource,
			@Mocked final DataRepository dataRepository,
			@Mocked final ProcessorChainReference chainReference)
			throws Exception {

		new Expectations() {{
			List<ProcessorReference> refs = new LinkedList<>();
			refs.add(newInstance(ProcessorReference.class, SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER, new HashMap<String, Object>()));
			refs.add(newInstance(ProcessorReference.class, FilterFactory.NAME_DB_INSERTION_FILTER, new HashMap<String, Object>()));
			refs.add(newInstance(ProcessorReference.class, FilterFactory.NAME_NOTIFICATION_FILTER, new HashMap<String, Object>()));

			chainReference.getProcessors();
			result = refs;

			adapterFactory.getClass();
			result = SourceAdapterFactory.class;

			filterFactory.getClass();
			result = FilterFactory.class;
		}};

		final ProcessorChainFactory chainFactory = new ProcessorChainFactory(adapterFactory, filterFactory);
		chainFactory.createProcessorChain(chainReference, dataSource, dataRepository);

		new Verifications() {{
			adapterFactory.createJsonSourceAdapter((DataSource) any, (String) any); times = 1;
			filterFactory.createDbInsertionFilter((DataSource) any, (DataRepository) any); times = 1;
			filterFactory.createNotificationFilter((DataSource) any); times = 1;
		}};
	}

}
