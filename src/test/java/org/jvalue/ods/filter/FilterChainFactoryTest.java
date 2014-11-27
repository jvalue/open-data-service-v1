package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.filter.reference.FilterChainMetaData;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.filter.reference.FilterReference;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class FilterChainFactoryTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testCreation(
			@Mocked final DataSource dataSource,
			@Mocked final SourceDataRepository dataRepository,
			@Mocked final FilterFactory filterFactory,
			@Mocked final Filter<Void, ArrayNode> startFilter,
			@Mocked final Filter<ArrayNode, ArrayNode> middleFilter,
			@Mocked FilterChainMetaData metaData) {

		new Expectations() {{
			filterFactory.createJsonSourceAdapter(dataSource); times = 1; result = startFilter;
			filterFactory.createDbInsertionFilter(dataSource, dataRepository); times = 1; result = middleFilter;
			startFilter.setNextFilter(middleFilter); times = 1; result = middleFilter;
			filterFactory.createNotificationFilter(dataSource); times = 1; result = middleFilter;
			middleFilter.setNextFilter(middleFilter); times = 1; result = middleFilter;
		}};

		List<FilterReference> references = new LinkedList<>();
		references.add(new FilterReference(FilterFactory.NAME_JSON_SOURCE_ADAPTER));
		references.add(new FilterReference(FilterFactory.NAME_DB_INSERTION_FILTER));
		references.add(new FilterReference(FilterFactory.NAME_NOTIFICATION_FILTER));
		final FilterChainReference chainReference = new FilterChainReference(references, metaData);

		final FilterChainFactory chainFactory = new FilterChainFactory(filterFactory);
		Filter<Void, ArrayNode> resultFilter = chainFactory.createFilterChain(chainReference, dataSource, dataRepository);
		Assert.assertTrue(resultFilter == startFilter);
	}

}
