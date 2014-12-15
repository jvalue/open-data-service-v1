package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.adapter.server.HttpServer;
import org.jvalue.ods.filter.adapter.server.Server;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;


abstract class AbstractSourceAdapterTest {

	protected final List<ObjectNode> testAdapterWithAllProtocols(
			final DataSource source,
			SourceAdapter adapter,
			String content) throws Exception {

		List<Server> serverList = new LinkedList<>();
		// serverList.add(new FtpServer());
		serverList.add(new HttpServer());

		List<ObjectNode> jsonResult = null;

		for (final Server server : serverList) {
			new Expectations() {{
				source.getUrl();
				result = server.getFileUrl();
			}};

			server.start(content);
			CollectorFilter collectorFilter = new CollectorFilter();
			adapter.setNextFilter(collectorFilter);
			adapter.process(null);
			server.stop();

			// all protocols should return the same result, even if parsed incorrectly
			if (jsonResult == null) jsonResult = collectorFilter.values;
			else Assert.assertEquals(jsonResult, collectorFilter.values);
		}

		// return json for checking parsing correctness
		return jsonResult;
	}


	private static final class CollectorFilter extends Filter<ObjectNode, ObjectNode> {

		private final List<ObjectNode> values = new LinkedList<>();

		@Override
		protected ObjectNode doProcess(ObjectNode node) {
			values.add(node);
			return node;
		}


		@Override
		protected void doOnComplete() {
			// nothing to do here
		}
	}

}
