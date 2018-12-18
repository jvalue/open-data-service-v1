package org.jvalue.ods.processor.adapter;


import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import org.jvalue.ods.api.processors.ProcessorType;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface SourceAdapterFactory {

	String	NAME_MULTI_SOURCE_ADAPTER = "MultiSourceAdapter",
			NAME_JSON_SOURCE_ADAPTER = "JsonSourceAdapter",
			NAME_CSV_SOURCE_ADAPTER = "CsvSourceAdapter",
			NAME_XML_SOURCE_ADAPTER = "XmlSourceAdapter",
			NAME_OSM_SOURCE_ADAPTER = "OsmSourceAdapter",
			NAME_PEGEL_PORTAL_MV_SOURCE_ADAPTER = "PegelPortalMvSourceAdapter",
			NAME_PEGEL_BRANDENBURG = "PegelBrandenburg",
			NAME_OPEN_WEATHER_MAP_SOURCE_ADAPTER = "OpenWeatherMapSourceAdapter",
			NAME_APIXU_SOURCE_ADAPTER = "APIXUSourceAdapter";

	String	ARGUMENT_SOURCE_URL = "sourceUrl",
			ARGUMENT_CSV_FORMAT = "csvFormat",
			ARGUMENT_LOCATIONS = "locations",
			ARGUMENT_API_KEY = "apiKey",
			ARGUMENT_MULTI_SOURCE = "sources";


	@CreationMethod(name = NAME_JSON_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_JSON_SOURCE_ADAPTER)
	SourceAdapter createJsonSourceAdapter(
		DataSource source,
		@Argument(ARGUMENT_SOURCE_URL) String sourceUrl);


	@CreationMethod(name = NAME_CSV_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_CSV_SOURCE_ADAPTER)
	SourceAdapter createCsvSourceAdapter(
			DataSource source,
			@Argument(ARGUMENT_SOURCE_URL) @Assisted(ARGUMENT_SOURCE_URL) String sourceUrl,
			@Argument(ARGUMENT_CSV_FORMAT) @Assisted(ARGUMENT_CSV_FORMAT) String csvFormat);


	@CreationMethod(name = NAME_XML_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_XML_SOURCE_ADAPTER)
	SourceAdapter createXmlSourceAdapter(
			DataSource source,
			@Argument(ARGUMENT_SOURCE_URL) String sourceUrl);


	@CreationMethod(name = NAME_OSM_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_OSM_SOURCE_ADAPTER)
	SourceAdapter createOsmSourceAdapter(
			DataSource source,
			@Argument(ARGUMENT_SOURCE_URL) String sourceUrl);


	@CreationMethod(name = NAME_PEGEL_PORTAL_MV_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_PEGEL_PORTAL_MV_SOURCE_ADAPTER)
	SourceAdapter createPegelPortalMvSourceAdapter(
			DataSource source,
			@Argument(ARGUMENT_SOURCE_URL) String sourceUrl);


	@CreationMethod(name = NAME_PEGEL_BRANDENBURG, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_PEGEL_BRANDENBURG)
	SourceAdapter createPegelBrandenburgAdapter(
			DataSource source,
			@Argument(ARGUMENT_SOURCE_URL) String sourceUrl);


	@CreationMethod(name = NAME_OPEN_WEATHER_MAP_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_OPEN_WEATHER_MAP_SOURCE_ADAPTER)
	SourceAdapter createOpenWeatherMapSourceAdapter(
		DataSource source,
		@Argument(ARGUMENT_LOCATIONS) @Assisted(ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		@Argument(ARGUMENT_API_KEY) @Assisted(ARGUMENT_API_KEY) String apiKey);


	@CreationMethod(name = NAME_APIXU_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_APIXU_SOURCE_ADAPTER)
	SourceAdapter createAPIXUSourceAdapter(
		DataSource source,
		@Argument(ARGUMENT_LOCATIONS) @Assisted(ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		@Argument(ARGUMENT_API_KEY) @Assisted(ARGUMENT_API_KEY) String apiKey);


	@CreationMethod(name = NAME_MULTI_SOURCE_ADAPTER, filterType = ProcessorType.SOURCE_ADAPTER)
	@Named(NAME_MULTI_SOURCE_ADAPTER)
	SourceAdapter createMultiSourceAdapter(
		DataSource source,
		@Argument(ARGUMENT_MULTI_SOURCE) @Assisted(ARGUMENT_MULTI_SOURCE) ArrayList<LinkedHashMap<String, Object>> sourceAdapters);

}
