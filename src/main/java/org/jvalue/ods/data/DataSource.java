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

import com.google.common.base.Objects;

import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.db.DbView;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.JsonPropertyKey;

import java.util.List;

public class DataSource {

	private final String id;
	private final String url;
	private final ObjectType dataSourceSchema;
	private final ObjectType rawDbSchema;
	private final ObjectType improvedDbSchema;
	private final OdsMetaData metaData;
	private final List<DbView> dbViews;
	private final JsonPropertyKey domainIdKey;

	public DataSource(
			String id,
			String url,
			ObjectType sourceschema,
			MapObjectType rawDbSchema,
			MapObjectType improvedDbSchema,
			OdsMetaData metaData,
			List<DbView> dbViews,
			JsonPropertyKey domainIdKey) {

		Assert.assertNotNull(id, url, sourceschema, rawDbSchema, improvedDbSchema, metaData, dbViews, domainIdKey);

		this.id = id;
		this.url = url;
		this.dataSourceSchema = sourceschema;
		this.rawDbSchema = rawDbSchema;
		this.improvedDbSchema = improvedDbSchema;
		this.metaData = metaData;
		this.dbViews = dbViews;
		this.domainIdKey = domainIdKey;
	}


	public String getId() {
		return id;
	}


	public String getUrl() {
		return url;
	}


	public ObjectType getDataSourceSchema() {
		return dataSourceSchema;
	}


	public ObjectType getImprovedDbSchema() {
		return improvedDbSchema;
	}


	public ObjectType getRawDbSchema() {
		return rawDbSchema;
	}


	public OdsMetaData getMetaData() {
		return metaData;
	}


	public List<DbView> getDbViews() {
		return dbViews;
	}


	public JsonPropertyKey getDomainIdKey() {
		return domainIdKey;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		if (other == this) return true;
		DataSource source = (DataSource) other;
		return Objects.equal(id, source.id)
				&& Objects.equal(url, source.url)
				&& Objects.equal(dataSourceSchema, source.dataSourceSchema)
				&& Objects.equal(rawDbSchema, source.rawDbSchema)
				&& Objects.equal(improvedDbSchema, source.improvedDbSchema)
				&& Objects.equal(metaData, source.metaData)
				&& Objects.equal(dbViews, source.dbViews)
				&& Objects.equal(domainIdKey, source.domainIdKey);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, url, dataSourceSchema, rawDbSchema, improvedDbSchema, metaData, dbViews, domainIdKey);
	}

}
