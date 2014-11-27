package org.jvalue.ods.filter.adapter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;


final class XmlSourceAdapter extends SourceAdapter {

	/*
	private static final DocumentBuilder documentBuilder;
	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch(ParserConfigurationException pce) {
			throw new RuntimeException(pce);
		}
	}
	*/


	@Inject
	XmlSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		/*
		try {
			Document doc = null;
			String sourceUrlString = dataSource.getUrl();
			if (!sourceUrlString.startsWith("http")) {
				URL sourceUrl = getClass().getResource(sourceUrlString);
				File xmlFile = new File(sourceUrl.toURI());
				doc = documentBuilder.parse(xmlFile);
			} else {
				String data = HttpUtils.readUrl(sourceUrlString, "UTF-8");
				doc = documentBuilder.parse(new InputSource(new StringReader(data)));
			}

			doc.getDocumentElement().normalize();
			// TODO needs conversion here

		} catch (Exception ex) {
			throw new FilterException("failed to parse XML", ex);
		}
		*/
		return null;
	}

}
