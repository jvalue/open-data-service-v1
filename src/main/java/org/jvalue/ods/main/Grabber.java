package org.jvalue.ods.main;

import java.util.List;

import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.Schema;

/**
 * The Interface Grabber.
 */
public interface Grabber {

	/**
	 * Gets the data source schema.
	 *
	 * @return the data source schema
	 */
	public Schema getDataSourceSchema();

	/**
	 * Creates the pegel online db schema.
	 * 
	 * @return the schema
	 */
	public MapSchema getDbSchema();

	/**
	 * Grab.
	 *
	 * @return the generic value
	 */
	public GenericValue grab();

	/**
	 * Gets the meta data.
	 *
	 * @return the meta data
	 */
	public OdsMetaData getMetaData();

	/**
	 * Gets the ods views.
	 *
	 * @return the ods views
	 */
	public List<OdsView> getOdsViews();

}