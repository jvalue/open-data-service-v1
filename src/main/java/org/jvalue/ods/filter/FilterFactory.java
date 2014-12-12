package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.filter.description.FilterArgument;
import org.jvalue.ods.filter.description.FilterCreationMethod;
import org.jvalue.ods.filter.description.FilterType;

public interface FilterFactory {

	static final String
			NAME_JSON_SOURCE_ADAPTER = "JsonSourceAdapter",
			NAME_CSV_SOURCE_ADAPTER = "CsvSourceAdapter",
			NAME_XML_SOURCE_ADAPTER = "XmlSourceAdapter",
			NAME_OSM_SOURCE_ADAPTER = "OsmSourceAdapter",
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter",
			NAME_INVALID_DOCUMENT_FILTER = "InvalidDocumentFilter";


	@FilterCreationMethod(name = NAME_JSON_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_JSON_SOURCE_ADAPTER)
	public Filter<Void, ObjectNode> createJsonSourceAdapter(DataSource source);


	@FilterCreationMethod(name = NAME_CSV_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_CSV_SOURCE_ADAPTER)
	public Filter<Void, ObjectNode> createCsvSourceAdapter(DataSource source, @FilterArgument("csvFormat") String csvFormat);


	@FilterCreationMethod(name = NAME_XML_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_XML_SOURCE_ADAPTER)
	public Filter<Void, ObjectNode> createXmlSourceAdapter(DataSource source);


	@FilterCreationMethod(name = NAME_OSM_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_OSM_SOURCE_ADAPTER)
	public Filter<Void, ObjectNode> createOsmSourceAdapter(DataSource source);



	@FilterCreationMethod(name = NAME_NOTIFICATION_FILTER, filterType =  FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_NOTIFICATION_FILTER)
	public Filter<ArrayNode, ArrayNode> createNotificationFilter(DataSource source);


	@FilterCreationMethod(name = NAME_DB_INSERTION_FILTER, filterType = FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_DB_INSERTION_FILTER)
	public Filter<ObjectNode, ObjectNode> createDbInsertionFilter(DataSource source, DataRepository dataRepository);


	@FilterCreationMethod(name = NAME_INVALID_DOCUMENT_FILTER, filterType = FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_INVALID_DOCUMENT_FILTER)
	public Filter<ArrayNode, ArrayNode> createInvalidDocumentFilter();

}
