package org.jvalue.ods.notifications;


import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.api.sources.DataSource;


/**
 * Describes classes that are capable of receiving data in a stream like fashion.
 */
public interface DataSink {

	/**
	 * Called when new data first becomes available. Signals the start of streaming and will
	 * be called once prior to any other methods of {@link org.jvalue.ods.notifications.DataSink}.
	 *
	 * @param source source of data
	 */
	public void onNewDataStart(DataSource source);

	/**
	 * Called when a new data item is available.
	 *
	 * @param source source of data
	 * @param data new data item
	 */
	public void onNewDataItem(DataSource source, ObjectNode data);

	/**
	 * Called when all data has been streamed to this object. Last call in {@link org.jvalue.ods.notifications.DataSink}.
	 *
	 * @param source source of data
	 */
	public void onNewDataComplete(DataSource source);

}
