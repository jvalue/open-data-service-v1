package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.filter.reference.FilterAnnotation;
import org.jvalue.ods.filter.reference.FilterType;

public interface FilterFactory {

	static final String
			NAME_JSON_SOURCE_ADAPTER = "JsonSourceAdapter",
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter";

	@FilterAnnotation(name = NAME_JSON_SOURCE_ADAPTER, filterType = FilterType.OUTPUT_FILTER)
	@Named(NAME_JSON_SOURCE_ADAPTER)
	public Filter<Void, ArrayNode> createJsonSourceAdapter(DataSource source);


	@FilterAnnotation(name = NAME_NOTIFICATION_FILTER, filterType =  FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_NOTIFICATION_FILTER)
	public Filter<ArrayNode, ArrayNode> createNotificationFilter(DataSource source);


	@FilterAnnotation(name = NAME_DB_INSERTION_FILTER, filterType = FilterType.INPUT_OUTPUT_FILTER)
	@Named(NAME_DB_INSERTION_FILTER)
	public Filter<ArrayNode, ArrayNode> createDbInsertionFilter(DataSource source, SourceDataRepository dataRepository);

}
