/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.data;

import java.util.List;

import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;

public abstract class DataSource {

	private final String id;
	private final String url;
	private final GenericValueType dataSourceSchema;
	private final MapComplexValueType dbSchema;
	private final OdsMetaData metaData;
	private final List<OdsView> odsViews;

	protected DataSource(
			String id,
			String url, 
			GenericValueType dataSourceSchema,
			MapComplexValueType dbSchema,
			OdsMetaData metaData,
			List<OdsView> odsViews) {

		if (id == null || url == null) throw new NullPointerException("id or url cannot be null");

		this.id = id;
		this.url = url;
		this.dataSourceSchema = dataSourceSchema;
		this.dbSchema = dbSchema;
		this.metaData = metaData;
		this.odsViews = odsViews;
	}


	public String getId() {
		return id;
	}


	public String getUrl() {
		return url;
	}


	public GenericValueType getDataSourceSchema() {
		return dataSourceSchema;
	}


	public MapComplexValueType getDbSchema() {
		return dbSchema;
	}


	public OdsMetaData getMetaData() {
		return metaData;
	}


	public List<OdsView> getOdsViews() {
		return odsViews;
	}


	
}
