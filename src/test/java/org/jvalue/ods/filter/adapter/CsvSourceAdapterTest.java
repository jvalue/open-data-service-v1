package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;

import java.util.List;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class CsvSourceAdapterTest extends AbstractSourceAdapterTest {

	@Test
	public void testDefaultAdapter(@Mocked final DataSource source) throws Exception {
		testAdapter("DEFAULT", ',', source);
	}


	@Test
	public void testExcelAdapter(@Mocked final DataSource source) throws Exception {
		testAdapter("EXCEL", ',', source);
	}


	@Test
	public void testRfcAdapter(@Mocked final DataSource source) throws Exception {
		testAdapter("RFC4180", ',', source);
	}


	@Test
	public void testTdfAdapter(@Mocked final DataSource source) throws Exception {
		testAdapter("TDF", '\t', source);
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


	private void testAdapter(
			String csvFormat,
			char csvDelimiter,
			DataSource source) throws Exception {

		CsvSourceAdapter adapter = new CsvSourceAdapter(source, csvFormat);
		List<ObjectNode> jsonResult = testAdapterWithAllProtocols(source, adapter, getCsvContent(csvDelimiter));

		Assert.assertEquals(2, jsonResult.size());

		ObjectNode node = jsonResult.get(0);
		Assert.assertEquals("value1", node.get("key1").asText());
		Assert.assertEquals("value2", node.get("key2").asText());

		node = jsonResult.get(1);
		Assert.assertEquals("value3", node.get("key1").asText());
		Assert.assertEquals("value4", node.get("key2").asText());
	}

}
