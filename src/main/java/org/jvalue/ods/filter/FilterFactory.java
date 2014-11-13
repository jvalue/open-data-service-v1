package org.jvalue.ods.filter;


import com.google.inject.name.Named;

import org.jvalue.ods.data.DataSource;

public interface FilterFactory {

	static final String NAME_NOTIFICATION_FILTER = "NotificationFilter";

	public @Named(NAME_NOTIFICATION_FILTER) Filter<Object, Object> createNotificationFilter(DataSource source);

}
