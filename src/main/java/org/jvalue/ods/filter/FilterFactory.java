package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
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
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter";


	@FilterCreationMethod(name = NAME_JSON_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_JSON_SOURCE_ADAPTER)
	public Filter<Void, ArrayNode> createJsonSourceAdapter(DataSource source);


	@FilterCreationMethod(name = NAME_CSV_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_CSV_SOURCE_ADAPTER)
	public Filter<Void, ArrayNode> createCsvSourceAdapter(DataSource source, @FilterArgument("csvFormat") String csvFormat);


	@FilterCreationMethod(name = NAME_XML_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_XML_SOURCE_ADAPTER)
	public Filter<Void, ArrayNode> createXmlSourceAdapter(DataSource source);



	@FilterCreationMethod(name = NAME_NOTIFICATION_FILTER, filterType =  FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_NOTIFICATION_FILTER)
	public Filter<ArrayNode, ArrayNode> createNotificationFilter(DataSource source);


	@FilterCreationMethod(name = NAME_DB_INSERTION_FILTER, filterType = FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_DB_INSERTION_FILTER)
	public Filter<ArrayNode, ArrayNode> createDbInsertionFilter(DataSource source, DataRepository dataRepository);

}
