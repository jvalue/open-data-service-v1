package org.jvalue.ods.processor.adapter.domain;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.AbstractSourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Data adapter for water level provided by the German state Brandenburg.
 */
public class BrandenburgPegelAdapter extends AbstractSourceAdapter {

	@Inject
	BrandenburgPegelAdapter(@Assisted DataSource dataSource, @Assisted String sourceUrl, MetricRegistry registry) {
		super(dataSource, sourceUrl, registry);
	}

	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new BrandenburgSourceIterator(source, sourceUrl, registry);
	}


	private static final class BrandenburgSourceIterator extends SourceIterator {

		private final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
		private LinkedList<Element> dataEntries = null;

		public BrandenburgSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
			super(source, sourceUrl, registry);
		}

		@Override
		protected JsonNode doNext() throws IOException {
			try {
				if (dataEntries == null) initData();
				Element data = dataEntries.removeFirst();
				ObjectNode newNode = jsonNodeFactory.objectNode();

				// get name
				newNode.put("name", data.getElementsByTagName("name").item(0).getTextContent());

				// get additional data entries
				NodeList details = ((Element) data.getElementsByTagName("ExtendedData").item(0)).getElementsByTagName("Data");
				for (int i = 0; i < details.getLength(); ++i) {
					Element element  = (Element) details.item(i);
					newNode.put(element.getAttribute("name"), element.getElementsByTagName("value").item(0).getTextContent());
				}

				// parse coordinates
				String coordinates = ((Element) data.getElementsByTagName("Point").item(0)).getElementsByTagName("coordinates").item(0).getTextContent();
				newNode.put("longitude", Double.valueOf(coordinates.split(",")[0]));
				newNode.put("latitude", Double.valueOf(coordinates.split(",")[1]));

				return newNode;

			} catch (ParserConfigurationException | IOException | SAXException e) {
				throw new SourceAdapterException(e);
			}
		}

		@Override
		protected boolean doHasNext() throws IOException {
			try {
				if (dataEntries == null) initData();
				return !dataEntries.isEmpty();

			} catch (ParserConfigurationException | IOException | SAXException e) {
				throw new SourceAdapterException(e);
			}
		}

		private void initData() throws ParserConfigurationException, IOException, SAXException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(sourceUrl.openStream());
			NodeList nodeList = ((Element) doc.getDocumentElement().getElementsByTagName("Document").item(0)).getElementsByTagName("Placemark");
			dataEntries = new LinkedList<>();
			for (int i = 0; i < nodeList.getLength(); ++i) {
				dataEntries.add((Element) nodeList.item(i));
			}
		}

	}

}
