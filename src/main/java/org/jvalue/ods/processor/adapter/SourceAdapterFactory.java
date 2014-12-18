package org.jvalue.ods.processor.adapter;


import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;
import org.jvalue.ods.processor.specification.ProcessorType;

public interface SourceAdapterFactory {

	static final String
			NAME_JSON_SOURCE_ADAPTER = "JsonSourceAdapter",
			NAME_CSV_SOURCE_ADAPTER = "CsvSourceAdapter",
			NAME_XML_SOURCE_ADAPTER = "XmlSourceAdapter",
			NAME_OSM_SOURCE_ADAPTER = "OsmSourceAdapter";


	@CreationMethod(name = NAME_JSON_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_JSON_SOURCE_ADAPTER)
	public SourceAdapter createJsonSourceAdapter(DataSource source);


	@CreationMethod(name = NAME_CSV_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_CSV_SOURCE_ADAPTER)
	public SourceAdapter createCsvSourceAdapter(DataSource source, @Argument("csvFormat") String csvFormat);


	@CreationMethod(name = NAME_XML_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_XML_SOURCE_ADAPTER)
	public SourceAdapter createXmlSourceAdapter(DataSource source);


	@CreationMethod(name = NAME_OSM_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_OSM_SOURCE_ADAPTER)
	public SourceAdapter createOsmSourceAdapter(DataSource source);

}
