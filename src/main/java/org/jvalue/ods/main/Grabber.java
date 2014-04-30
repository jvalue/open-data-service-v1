package org.jvalue.ods.main;

import java.util.List;

import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.Schema;

public interface Grabber {

	public Schema getDataSourceSchema();

	/**
	 * Creates the pegel online db schema.
	 * 
	 * @return the schema
	 */
	public MapSchema getDbSchema();

	public GenericValue grab();

	public OdsMetaData getMetaData();

	public List<OdsView> getOdsViews();

}