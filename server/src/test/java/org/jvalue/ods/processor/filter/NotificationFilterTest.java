package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.NotificationManager;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class NotificationFilterTest {

	@Mocked
	private MetricRegistry registry;

	@Test
	public void testForwarding(
			@Mocked final DataSource source,
			@Mocked final NotificationManager manager) throws Exception {

		Filter<ObjectNode, ObjectNode> filter = new NotificationFilter(source, manager, registry);
		final ObjectNode data = new ObjectNode(JsonNodeFactory.instance);
		filter.filter(data);

		new Verifications() {{
			manager.onNewDataItem(source, data);
		}};
	}

}
