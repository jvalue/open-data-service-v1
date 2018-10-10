package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Mocked;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.server.HttpServer;
import org.jvalue.ods.processor.adapter.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiSourceAdapterTest {

	@Mocked
	private MetricRegistry registry;
	@Mocked
	private DataSource source;
	private SourceAdapterFactory sourceAdapterFactory;

	private static final ArrayNode JSON_ARRAY_1;
	private static final ArrayNode JSON_ARRAY_2;
	private static final ObjectNode JSON_OBJECT_1;
	private static final ObjectNode JSON_OBJECT_2;

	private static final int SERVER_PORT_1 = 65532;
	private static final int SERVER_PORT_2 = 65533;
	private static final int SERVER_PORT_3 = 65534;

	private static List<Server> serverList;

	private static final String JSON_SOURCE_NAME_1 = "FirstJsonSourceAdapter";
	private static final String JSON_SOURCE_NAME_2 = "SecondJsonSourceAdapter";
	private static final String XML_SOURCE_NAME = "XmlSource";

	static {
		JSON_ARRAY_1 = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = JSON_ARRAY_1.addObject();
		jsonObject.put("array1key1", "value1");
		jsonObject.put("array1key2", "value2");
		jsonObject = JSON_ARRAY_1.addObject();
		jsonObject.put("array1key3", "value1");
		jsonObject.put("array1key4", "value2");
	}


	static {
		JSON_ARRAY_2 = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = JSON_ARRAY_2.addObject();
		jsonObject.put("array2key1", "value1");
		jsonObject.put("array2key2", "value2");
		jsonObject = JSON_ARRAY_2.addObject();
		jsonObject.put("array2key3", "value1");
		jsonObject.put("array2key4", "value2");
	}


	static {
		JSON_OBJECT_1 = new ObjectNode(JsonNodeFactory.instance);
		JSON_OBJECT_1.put("object1key5", "value11");
		JSON_OBJECT_1.put("object1key6", "value42");
		ObjectNode jsonObject = JSON_OBJECT_1.putObject("person");
		jsonObject.put("object1name", "myName");

	}


	static {
		JSON_OBJECT_2 = new ObjectNode(JsonNodeFactory.instance);
		JSON_OBJECT_2.put("object2key5", "value11");
		JSON_OBJECT_2.put("object2key6", "value42");
		ObjectNode jsonObject = JSON_OBJECT_2.putObject("person");
		jsonObject.put("object2name", "myName");
	}

	@Before
	public void setUp() {
		serverList = new ArrayList<>();
		sourceAdapterFactory = new SourceAdapterFactoryImpl(registry);
	}


	@Test
	public void testMultiJsonSourceAdapterObject() throws Exception {

		ArrayList<LinkedHashMap> sourceAdapters = new ArrayList<>();

		LinkedHashMap<String, String> firstJsonEndpoint = new LinkedHashMap<>();
		HttpServer httpServer1 = createHttpServer(JSON_OBJECT_1.toString(), SERVER_PORT_1);
		firstJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		firstJsonEndpoint.put("sourceUrl", httpServer1.getFileUrl().toString());


		LinkedHashMap<String, Object> sendNode = new LinkedHashMap<>();
		sendNode.put("source", firstJsonEndpoint);
		sendNode.put("alias", JSON_SOURCE_NAME_1);


		LinkedHashMap<String, String> secondJsonEndpoint = new LinkedHashMap<>();
		HttpServer httpServer2 = createHttpServer(JSON_OBJECT_2.toString(), SERVER_PORT_2);
		secondJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		secondJsonEndpoint.put("sourceUrl", httpServer2.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode2 = new LinkedHashMap<>();
		sendNode2.put("source", secondJsonEndpoint);
		sendNode2.put("alias", JSON_SOURCE_NAME_2);

		sourceAdapters.add(sendNode);
		sourceAdapters.add(sendNode2);

		MultiSourceAdapter adapter = createAdapter(sourceAdapters);

		ObjectNode result = getResult(adapter);

		Assert.assertEquals(JSON_OBJECT_1, result.get(JSON_SOURCE_NAME_1));
		Assert.assertEquals(JSON_OBJECT_2, result.get(JSON_SOURCE_NAME_2));
	}


	@Test
	public void testMultiJsonSourceAdapterArray() throws Exception {

		ArrayList<LinkedHashMap> sourceAdapters = new ArrayList<>();

		LinkedHashMap<String, String> firstJsonEndpoint = new LinkedHashMap<>();
		HttpServer ArrayHttpServer1 = createHttpServer(JSON_ARRAY_1.toString(), SERVER_PORT_1);
		firstJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		firstJsonEndpoint.put("sourceUrl", ArrayHttpServer1.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode = new LinkedHashMap<>();
		sendNode.put("source", firstJsonEndpoint);
		sendNode.put("alias", JSON_SOURCE_NAME_1);


		LinkedHashMap<String, String> secondJsonEndpoint = new LinkedHashMap<>();
		HttpServer ArrayHttpServer2 = createHttpServer(JSON_ARRAY_2.toString(), SERVER_PORT_2);
		secondJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		secondJsonEndpoint.put("sourceUrl", ArrayHttpServer2.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode2 = new LinkedHashMap<>();
		sendNode2.put("source", secondJsonEndpoint);
		sendNode2.put("alias", JSON_SOURCE_NAME_2);

		sourceAdapters.add(sendNode);
		sourceAdapters.add(sendNode2);


		MultiSourceAdapter adapter = createAdapter(sourceAdapters);

		ObjectNode result = getResult(adapter);

		Assert.assertEquals(JSON_ARRAY_1, result.get(JSON_SOURCE_NAME_1));
		Assert.assertEquals(JSON_ARRAY_2, result.get(JSON_SOURCE_NAME_2));
	}


	@Test
	public void testMultiJsonSourceAdapterArrayAndObject() throws Exception {

		ArrayList<LinkedHashMap> sourceAdapters = new ArrayList<>();

		LinkedHashMap<String, String> firstJsonEndpoint = new LinkedHashMap<>();
		HttpServer arrayHttpServer1 = createHttpServer(JSON_ARRAY_1.toString(), SERVER_PORT_1);
		firstJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		firstJsonEndpoint.put("sourceUrl", arrayHttpServer1.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode = new LinkedHashMap<>();
		sendNode.put("source", firstJsonEndpoint);
		sendNode.put("alias", JSON_SOURCE_NAME_1);

		LinkedHashMap<String, String> secondJsonEndpoint = new LinkedHashMap<>();

		HttpServer objectHttpServer2 = createHttpServer(JSON_OBJECT_2.toString(), SERVER_PORT_2);
		secondJsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		secondJsonEndpoint.put("sourceUrl", objectHttpServer2.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode2 = new LinkedHashMap<>();
		sendNode2.put("source", secondJsonEndpoint);
		sendNode2.put("alias", JSON_SOURCE_NAME_2);


		sourceAdapters.add(sendNode);
		sourceAdapters.add(sendNode2);

		MultiSourceAdapter adapter = createAdapter(sourceAdapters);

		ObjectNode result = getResult(adapter);


		Assert.assertEquals(JSON_ARRAY_1, result.get(JSON_SOURCE_NAME_1));
		Assert.assertEquals(JSON_OBJECT_2, result.get(JSON_SOURCE_NAME_2));
	}


	@Test
	public void testMultiJsonSourceAndXmlSourceAdapter() throws Exception {

		ArrayList<LinkedHashMap> sourceAdapters = new ArrayList<>();

		LinkedHashMap<String, String> jsonEndpoint = new LinkedHashMap<>();

		HttpServer objectHttpServer2 = createHttpServer(JSON_OBJECT_1.toString(), SERVER_PORT_1);
		jsonEndpoint.put("name", SourceAdapterFactory.NAME_JSON_SOURCE_ADAPTER);
		jsonEndpoint.put("sourceUrl", objectHttpServer2.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode = new LinkedHashMap<>();
		sendNode.put("source", jsonEndpoint);
		sendNode.put("alias", JSON_SOURCE_NAME_1);


		LinkedHashMap<String, String> xmlEndpoint = new LinkedHashMap<>();

		String xmlContent = "<list><item><key>value1</key></item><item><key>value2</key></item></list>";
		HttpServer xmlHttpServer3 = createHttpServer(xmlContent, SERVER_PORT_3);
		xmlEndpoint.put("name", SourceAdapterFactory.NAME_XML_SOURCE_ADAPTER);
		xmlEndpoint.put("sourceUrl", xmlHttpServer3.getFileUrl().toString());

		LinkedHashMap<String, Object> sendNode2 = new LinkedHashMap<>();
		sendNode2.put("source", xmlEndpoint);
		sendNode2.put("alias", XML_SOURCE_NAME);

		sourceAdapters.add(sendNode);
		sourceAdapters.add(sendNode2);

		MultiSourceAdapter adapter = createAdapter(sourceAdapters);

		ObjectNode result = getResult(adapter);

		Assert.assertEquals(JSON_OBJECT_1, result.get(JSON_SOURCE_NAME_1));
		Assert.assertEquals("value1", result.get("XmlSource").get(0).get("key").asText());
		Assert.assertEquals("value2", result.get("XmlSource").get(1).get("key").asText());
	}


	private ObjectNode getResult(MultiSourceAdapter adapter) {
		ObjectNode result = adapter.iterator().next();
		return result;
	}


	private HttpServer createHttpServer(String content, int port) throws Exception {
		HttpServer fakeHttpServer = new HttpServer();
		fakeHttpServer.start(content, port);
		serverList.add(fakeHttpServer);
		return fakeHttpServer;
	}


	protected MultiSourceAdapter createAdapter(ArrayList<LinkedHashMap> sourceAdapters) {
		return new MultiSourceAdapter(sourceAdapterFactory, source, sourceAdapters, registry);
	}


	@After
	public void tearDown() throws Exception {
		for (Server server : serverList) {
			server.stop();
		}
	}


	// could be replaced with a Mock but didn't work for me with jMockit
	private final class SourceAdapterFactoryImpl implements SourceAdapterFactory {

		private final MetricRegistry registry;


		public SourceAdapterFactoryImpl(MetricRegistry registry) {
			this.registry = registry;
		}


		@Override
		public SourceAdapter createJsonSourceAdapter(DataSource source, String sourceUrl) {
			return new JsonSourceAdapter(source, sourceUrl, registry);
		}


		@Override
		public SourceAdapter createCsvSourceAdapter(DataSource source, String sourceUrl, String csvFormat) {
			return null;
		}


		@Override
		public SourceAdapter createXmlSourceAdapter(DataSource source, String sourceUrl) {
			return new XmlSourceAdapter(source, sourceUrl, registry);
		}


		@Override
		public SourceAdapter createOsmSourceAdapter(DataSource source, String sourceUrl) {
			return null;
		}


		@Override
		public SourceAdapter createPegelPortalMvSourceAdapter(DataSource source, String sourceUrl) {
			return null;
		}


		@Override
		public SourceAdapter createPegelBrandenburgAdapter(DataSource source, String sourceUrl) {
			return null;
		}


		@Override
		public SourceAdapter createOpenWeatherMapSourceAdapter(DataSource source, ArrayList<LinkedHashMap<String, String>> locations, String apiKey) {
			return null;
		}


		@Override
		public SourceAdapter createAPIXUSourceAdapter(DataSource source, ArrayList<LinkedHashMap<String, String>> locations, String apiKey) {
			return null;
		}


		@Override
		public SourceAdapter createMultiSourceAdapter(DataSource source, ArrayList<LinkedHashMap> sourceAdapters) {
			return null;
		}
	}
}
