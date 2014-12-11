package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Assert;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.adapter.server.FtpServer;
import org.jvalue.ods.filter.adapter.server.HttpServer;
import org.jvalue.ods.filter.adapter.server.Server;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;


abstract class AbstractSourceAdapterTest {

	protected final ArrayNode testAdapterWithAllProtocols(
			final DataSource source,
			SourceAdapter adapter,
			String content) throws Exception {

		ArrayNode jsonResult = null;

		List<Server> serverList = new LinkedList<>();
		serverList.add(new FtpServer());
		serverList.add(new HttpServer());

		for (final Server server : serverList) {
			new Expectations() {{
				source.getUrl();
				result = server.getFileUrl();
			}};

			server.start(content);
			ArrayNode json = adapter.grabSource();
			server.stop();

			if (jsonResult == null) jsonResult = json;
			else Assert.assertEquals(jsonResult, json);
		}

		return jsonResult;
	}

}
