package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.adapter.server.FtpServer;
import org.jvalue.ods.filter.adapter.server.HttpServer;
import org.jvalue.ods.filter.adapter.server.Server;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public abstract class AbstractSourceAdapterTest {

	@Test
	public final void testAdapter(@Mocked final DataSource source) throws Exception {
		List<Server> serverList = new LinkedList<>();
		serverList.add(new FtpServer());
		serverList.add(new HttpServer());

		for (final Server server : serverList) {
			new Expectations() {{
				source.getUrl();
				result = server.getFileUrl();
			}};

			server.start(getContent());

			SourceAdapter adapter = getSourceAdapter(source);
			ArrayNode jsonArray = adapter.grabSource();
			assertEqualsContent(jsonArray);

			server.stop();
		}
	}


	protected abstract SourceAdapter getSourceAdapter(DataSource source);

	protected abstract String getContent();

	protected abstract void assertEqualsContent(ArrayNode jsonArray);

}
