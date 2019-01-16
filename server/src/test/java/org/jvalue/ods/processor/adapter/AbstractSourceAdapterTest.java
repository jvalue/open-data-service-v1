/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.jvalue.ods.processor.adapter.server.FtpServer;
import org.jvalue.ods.processor.adapter.server.HttpServer;
import org.jvalue.ods.processor.adapter.server.Server;

import java.util.LinkedList;
import java.util.List;


abstract class AbstractSourceAdapterTest {

	protected final List<ObjectNode> testAdapterWithAllProtocols(String content) throws Exception {

		List<Server> serverList = new LinkedList<>();
		serverList.add(new FtpServer());
		serverList.add(new HttpServer());

		List<ObjectNode> jsonResult = null;

		for (final Server server : serverList) {
			server.start(content);
			List<ObjectNode> tmpJsonResult = new LinkedList<>();
			for (ObjectNode node : createAdapter(server.getFileUrl().toString())) tmpJsonResult.add(node);
			server.stop();

			// all protocols should return the same result, even if parsed incorrectly
			if (jsonResult == null) jsonResult = tmpJsonResult;
			else Assert.assertEquals(jsonResult, tmpJsonResult);
		}

		// return json for checking parsing correctness
		return jsonResult;
	}


	protected abstract SourceAdapter createAdapter(String sourceUrl);

}
