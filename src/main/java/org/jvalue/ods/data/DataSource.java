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
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.db.OdsView;
import org.jvalue.ods.utils.Assert;

public class DataSource {

	private final String id;
	private final String url;
	private final ObjectType dataSourceSchema;
	private final ObjectType rawDbSchema;
	private final ObjectType improvedDbSchema;
	private final OdsMetaData metaData;
	private final List<OdsView> odsViews;

	public DataSource(String id, String url, ObjectType sourceschema,
			MapObjectType rawDbSchema, MapObjectType improvedDbSchema,
			OdsMetaData metaData, List<OdsView> odsViews) {

		Assert.assertNotNull(id, url, sourceschema, rawDbSchema,
				improvedDbSchema, metaData, odsViews);

		this.id = id;
		this.url = url;
		this.dataSourceSchema = sourceschema;
		this.rawDbSchema = rawDbSchema;
		this.improvedDbSchema = improvedDbSchema;
		this.metaData = metaData;
		this.odsViews = odsViews;
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

	public List<OdsView> getOdsViews() {
		return odsViews;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource))
			return false;
		DataSource source = (DataSource) other;
		return source.id.equals(id) && source.url.equals(url)
				&& source.dataSourceSchema.equals(dataSourceSchema)
				&& source.rawDbSchema.equals(rawDbSchema)
				&& source.improvedDbSchema.equals(improvedDbSchema)
				&& source.metaData.equals(metaData)
				&& source.odsViews.equals(odsViews);
	}

	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * id.hashCode();
		hash = hash + MULT * url.hashCode();
		hash = hash + MULT * dataSourceSchema.hashCode();
		hash = hash + MULT * rawDbSchema.hashCode();
		hash = hash + MULT * improvedDbSchema.hashCode();
		hash = hash + MULT * metaData.hashCode();
		hash = hash + MULT * odsViews.hashCode();
		return hash;
	}

}
