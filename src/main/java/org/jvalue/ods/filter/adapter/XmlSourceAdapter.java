package org.jvalue.ods.filter.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;

import java.io.IOException;
import java.util.List;


final class XmlSourceAdapter extends SourceAdapter {

	private static final ObjectMapper
			xmlMapper = new XmlMapper(),
			jsonMapper = new ObjectMapper();


	@Inject
	XmlSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		try {
			List<Object> xmlValues = xmlMapper.readValue(dataSource.getUrl(), new TypeReference<List<Object>>() {} );
			return jsonMapper.valueToTree(xmlValues);
		} catch (IOException e) {
			throw new FilterException(e);
		}
	}

}
