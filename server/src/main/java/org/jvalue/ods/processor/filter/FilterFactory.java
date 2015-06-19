package org.jvalue.ods.processor.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.name.Named;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;
import org.jvalue.ods.api.processors.ProcessorType;

public interface FilterFactory {

	static final String
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter",
			NAME_INT_TO_STRING_KEY_FILTER = "IntToStringKeyFilter",
			NAME_INVALID_DOCUMENT_FILTER = "InvalidDocumentFilter",
			NAME_PEGEL_ONLINE_MERGER = "PegelOnlineMerger",
			NAME_PEGEL_BRANDENBURG_MERGER = "PegelBrandenburgMerger";


	@CreationMethod(name = NAME_NOTIFICATION_FILTER, filterType =  ProcessorType.FILTER)
	@Named(NAME_NOTIFICATION_FILTER)
	public Filter<ObjectNode, ObjectNode> createNotificationFilter(DataSource source);


	@CreationMethod(name = NAME_DB_INSERTION_FILTER, filterType = ProcessorType.FILTER)
	@Named(NAME_DB_INSERTION_FILTER)
	public Filter<ObjectNode, ObjectNode> createDbInsertionFilter(DataSource source, DataRepository dataRepository, @Argument("updateData") boolean updateDataIfExists);


	@CreationMethod(name = NAME_INT_TO_STRING_KEY_FILTER, filterType = ProcessorType.FILTER)
	@Named(NAME_INT_TO_STRING_KEY_FILTER)
	public Filter<ObjectNode, ObjectNode> createIntToStringKeyFilter(DataSource source);


	@CreationMethod(name = NAME_INVALID_DOCUMENT_FILTER, filterType = ProcessorType.FILTER)
	@Named(NAME_INVALID_DOCUMENT_FILTER)
	public Filter<ArrayNode, ArrayNode> createInvalidDocumentFilter(DataSource source);


	@CreationMethod(name = NAME_PEGEL_ONLINE_MERGER, filterType = ProcessorType.FILTER)
	@Named(NAME_PEGEL_ONLINE_MERGER)
	public Filter<ObjectNode, ObjectNode> createPegelOnlineMerger(DataSource source);


	@CreationMethod(name = NAME_PEGEL_BRANDENBURG_MERGER, filterType = ProcessorType.FILTER)
	@Named(NAME_PEGEL_BRANDENBURG_MERGER)
	public Filter<ObjectNode, ObjectNode> createPegelBrandenburgMerger(DataSource source);

}
