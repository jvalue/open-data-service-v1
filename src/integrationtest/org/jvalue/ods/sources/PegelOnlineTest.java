package org.jvalue.ods.sources;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.Filter;
import org.jvalue.ods.rest.model.FilterChain;

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

		final FilterChain filterChain = new FilterChain();
		filterChain.filters = new LinkedList<>();
		filterChain.executionInterval = new FilterChainExecutionInterval(100, TimeUnit.SECONDS);

		final Filter adapterFilter = new Filter();
		adapterFilter.name = "JsonSourceAdapter";

		final Filter dbFilter = new Filter();
		dbFilter.name = "DbInsertionFilter";

		final Filter notificationFilter = new Filter();
		notificationFilter.name = "NotificationFilter";

		filterChain.filters.add(adapterFilter);
		filterChain.filters.add(dbFilter);
		filterChain.filters.add(notificationFilter);

		runTest(source, filterChain, 2000);
	}

}
