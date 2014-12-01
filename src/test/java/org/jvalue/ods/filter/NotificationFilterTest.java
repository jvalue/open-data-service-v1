package org.jvalue.ods.filter;

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

	@Test
	public void testForwarding(
			@Mocked final DataSource source,
			@Mocked final NotificationManager manager) throws Exception {

		Filter<ArrayNode, ArrayNode> filter = new NotificationFilter(manager, source);
		final ArrayNode data = new ArrayNode(JsonNodeFactory.instance);
		filter.filter(data);

		new Verifications() {{
			manager.notifySourceChanged(source, data);
		}};
	}

}
