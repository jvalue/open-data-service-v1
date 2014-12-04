package org.jvalue.ods.filter;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.filter.reference.FilterReference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static mockit.Deencapsulation.newInstance;

@RunWith(JMockit.class)
public final class FilterChainFactoryTest {

	@Test
	public void testCreation(
			@Mocked final FilterFactory filterFactory,
			@Mocked final DataSource dataSource,
			@Mocked final DataRepository dataRepository,
			@Mocked final FilterChainReference chainReference)
			throws Exception {

		new Expectations() {{
			List<FilterReference> refs = new LinkedList<>();
			refs.add(newInstance(FilterReference.class, FilterFactory.NAME_JSON_SOURCE_ADAPTER, new HashMap<String, Object>()));
			refs.add(newInstance(FilterReference.class, FilterFactory.NAME_DB_INSERTION_FILTER, new HashMap<String, Object>()));
			refs.add(newInstance(FilterReference.class, FilterFactory.NAME_NOTIFICATION_FILTER, new HashMap<String, Object>()));

			chainReference.getFilterReferences();
			result = refs;
		}};

		final FilterChainFactory chainFactory = new FilterChainFactory(filterFactory);
		chainFactory.createFilterChain(chainReference, dataSource, dataRepository);

		new Verifications() {{
			filterFactory.createJsonSourceAdapter((DataSource) any); times = 1;
			filterFactory.createDbInsertionFilter((DataSource) any, (DataRepository) any); times = 1;
			filterFactory.createNotificationFilter((DataSource) any); times = 1;
		}};
	}

}
