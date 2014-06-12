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


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		DataSource source = (DataSource) other;
		return equals(id, source.id)
			&& equals(url, source.url) 
			&& equals(dataSourceSchema, source.dataSourceSchema)
			&& equals(metaData, source.metaData);
	}


	private boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}


	@Override
	public int hashCode() {
		final int MULT = 13;
		int hash = 11;
		hash = hash * MULT + id.hashCode();
		hash = hash * MULT + url.hashCode();
		hash = hash * MULT + (dataSourceSchema == null ? 0 : dataSourceSchema.hashCode());
		hash = hash * MULT + (metaData == null ? 0 : metaData.hashCode());
		return hash;
	}
}
