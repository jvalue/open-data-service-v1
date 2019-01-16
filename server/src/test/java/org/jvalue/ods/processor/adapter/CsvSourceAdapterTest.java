/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;


@RunWith(JMockit.class)
public final class CsvSourceAdapterTest extends AbstractSourceAdapterTest {

	@Mocked private MetricRegistry registry;
	@Mocked private DataSource source;

	@Test
	public void testDefaultAdapter() throws Exception {
		testAdapter("DEFAULT", ',');
	}


	@Test
	public void testExcelAdapter() throws Exception {
		testAdapter("EXCEL", ',');
	}


	@Test
	public void testRfcAdapter() throws Exception {
		testAdapter("RFC4180", ',');
	}


	@Test
	public void testTdfAdapter() throws Exception {
		testAdapter("TDF", '\t');
	}


	private String getCsvContent(char delimiter) {
		StringBuilder builder = new StringBuilder();
		builder.append("key1");
		builder.append(delimiter);
		builder.append("key2\n");
		builder.append("value1");
		builder.append(delimiter);
		builder.append("value2\n");
		builder.append("value3");
		builder.append(delimiter);
		builder.append("value4\n");
		return builder.toString();
	}


	private String currentCsvFormat;

	private void testAdapter(String csvFormat, char csvDelimiter) throws Exception {
		currentCsvFormat = csvFormat;
		List<ObjectNode> jsonResult = testAdapterWithAllProtocols(getCsvContent(csvDelimiter));

		Assert.assertEquals(2, jsonResult.size());

		ObjectNode node = jsonResult.get(0);
		Assert.assertEquals("value1", node.get("key1").asText());
		Assert.assertEquals("value2", node.get("key2").asText());

		node = jsonResult.get(1);
		Assert.assertEquals("value3", node.get("key1").asText());
		Assert.assertEquals("value4", node.get("key2").asText());
	}


	@Override
	protected SourceAdapter createAdapter(String sourceUrl) {
		return new CsvSourceAdapter(source, sourceUrl, currentCsvFormat, registry);
	}
}
