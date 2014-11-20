package org.jvalue.ods.filter.adapter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.HttpUtils;
import org.jvalue.ods.utils.Log;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


final class XmlSourceAdapter extends SourceAdapter<Document> {


	@Inject
	XmlSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public Document grabSource() {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = null;
			String sourceUrlString = dataSource.getUrl();
			if (!sourceUrlString.startsWith("http")) {
				URL sourceUrl = getClass().getResource(sourceUrlString);
				File xmlFile = new File(sourceUrl.toURI());
				doc = dBuilder.parse(xmlFile);
			} else {
				String data = HttpUtils.readUrl(sourceUrlString, "UTF-8");
				doc = dBuilder.parse(new InputSource(new StringReader(data)));
			}

			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception ex) {
			Log.info(ex.getMessage());
			return null;
		}

	}

}
