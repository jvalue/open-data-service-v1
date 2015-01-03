package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
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

		AbstractFilter<ArrayNode, ArrayNode> filter = new NotificationFilter(source, manager, registry);
		final ArrayNode data = new ArrayNode(JsonNodeFactory.instance);
		filter.filter(data);

		new Verifications() {{
			manager.notifySourceChanged(source, data);
		}};
	}

}
