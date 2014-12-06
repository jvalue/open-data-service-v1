package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;

import java.io.File;
import java.io.PrintWriter;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class CsvSourceAdapterTest {

	private static final String FILE_NAME = CsvSourceAdapterTest.class.getSimpleName();


	@After
	public void deleteCsvFile() throws Exception {
		File file = new File(FILE_NAME);
		file.deleteOnExit();
	}


	@Test
	public void testDefaultAdapter(
			@Mocked final DataSource source) throws Exception {

		createCsvFile(',');
		testAdapter("DEFAULT", source);
	}


	@Test
	public void testExcelAdapter(
			@Mocked final DataSource source) throws Exception {

		createCsvFile(',');
		testAdapter("EXCEL", source);
	}


	@Test
	public void testRfcAdapter(
			@Mocked final DataSource source) throws Exception {

		createCsvFile(',');
		testAdapter("RFC4180", source);
	}


	@Test
	public void testTdfAdapter(
			@Mocked final DataSource source) throws Exception {

		createCsvFile('\t');
		testAdapter("TDF", source);
	}


	private void createCsvFile(char delimiter) throws Exception {
		PrintWriter writer = new PrintWriter(FILE_NAME, "UTF-8");
		writer.println("key1" + delimiter + "key2");
		writer.println("value1" + delimiter + "value2");
		writer.println("value3" + delimiter + "value4");
		writer.close();
	}


	private void testAdapter(
			String csvFormat,
			final DataSource source) throws Exception {

		new Expectations() {{
			source.getUrl();
			result = new File(FILE_NAME).toURI().toURL();
		}};

		CsvSourceAdapter adapter = new CsvSourceAdapter(source, csvFormat);
		ArrayNode array = adapter.grabSource();

		Assert.assertEquals(2, array.size());

		ObjectNode node = (ObjectNode) array.get(0);
		Assert.assertEquals("value1", node.get("key1").asText());
		Assert.assertEquals("value2", node.get("key2").asText());

		node = (ObjectNode) array.get(1);
		Assert.assertEquals("value3", node.get("key1").asText());
		Assert.assertEquals("value4", node.get("key2").asText());
	}

}
