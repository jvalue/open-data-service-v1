package org.jvalue.ods.processor.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.processor.specification.CreationMethod;
import org.jvalue.ods.processor.specification.ProcessorType;

public interface FilterFactory {

	static final String
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter",
			NAME_INVALID_DOCUMENT_FILTER = "InvalidDocumentFilter";


	@CreationMethod(name = NAME_NOTIFICATION_FILTER, filterType =  ProcessorType.FILTER)
	@Named(NAME_NOTIFICATION_FILTER)
	public Filter<ArrayNode, ArrayNode> createNotificationFilter(DataSource source);


	@CreationMethod(name = NAME_DB_INSERTION_FILTER, filterType = ProcessorType.FILTER)
	@Named(NAME_DB_INSERTION_FILTER)
	public Filter<ObjectNode, ObjectNode> createDbInsertionFilter(DataSource source, DataRepository dataRepository);


	@CreationMethod(name = NAME_INVALID_DOCUMENT_FILTER, filterType = ProcessorType.FILTER)
	@Named(NAME_INVALID_DOCUMENT_FILTER)
	public Filter<ArrayNode, ArrayNode> createInvalidDocumentFilter(DataSource source);

}
