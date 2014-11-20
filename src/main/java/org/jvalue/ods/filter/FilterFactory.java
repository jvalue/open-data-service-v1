package org.jvalue.ods.filter;


import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.SourceDataRepository;

public interface FilterFactory {

	static final String
			NAME_DB_INSERTION_FILTER = "DbInsertionFilter",
			NAME_NOTIFICATION_FILTER = "NotificationFilter";

	public @Named(NAME_NOTIFICATION_FILTER) Filter<Object, Object> createNotificationFilter(DataSource source);
	public @Named(NAME_DB_INSERTION_FILTER) Filter<Object, Object> createDbInsertionFilter(
			DataSource source,
			SourceDataRepository dataRepository);

}
