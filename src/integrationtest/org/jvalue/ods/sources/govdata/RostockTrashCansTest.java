package org.jvalue.ods.sources.govdata;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.Filter;
import org.jvalue.ods.rest.model.FilterChain;
import org.jvalue.ods.sources.AbstractDataSourceTest;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public final class RostockTrashCansTest extends AbstractDataSourceTest {

	@Test
	public void testTrashCanSource() throws Exception {
		final DataSource source = new DataSource();
		source.url = new URL("https://geo.sv.rostock.de/download/opendata/abfallbehaelter/abfallbehaelter.csv");
		source.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
		source.domainIdKey = "/id";
		source.schema = new ObjectNode(JsonNodeFactory.instance);

		final FilterChain filterChain = new FilterChain();
		filterChain.filters = new LinkedList<>();
		filterChain.executionInterval = new FilterChainExecutionInterval(100, TimeUnit.SECONDS);

		final Filter adapterFilter = new Filter();
		adapterFilter.name = "CsvSourceAdapter";
		adapterFilter.arguments = new HashMap<>();
		adapterFilter.arguments.put("csvFormat", "DEFAULT");

		final Filter dbFilter = new Filter();
		dbFilter.name = "DbInsertionFilter";

		filterChain.filters.add(adapterFilter);
		filterChain.filters.add(dbFilter);

		runTest(source, filterChain, 2000);
	}

}
