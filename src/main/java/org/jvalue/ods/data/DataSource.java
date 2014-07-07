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
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.utils.Assert;

public abstract class DataSource implements Cloneable {

	private final String id;
	private final String url;
	private final ObjectType dataSourceSchema;
	private final ObjectType dbSchema;
	private final OdsMetaData metaData;
	private final List<OdsView> odsViews;

	protected DataSource(
			String id, 
			String url, 
			ObjectType sourceschema,
			ObjectType dbschema, 
			OdsMetaData metaData, 
			List<OdsView> odsViews) {

		Assert.assertNotNull(id, url, sourceschema, dbschema, metaData, odsViews);

		this.id = id;
		this.url = url;
		this.dataSourceSchema = sourceschema;
		this.dbSchema = dbschema;
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

	public ObjectType getDbSchema() {
		return dbSchema;
	}

	public OdsMetaData getMetaData() {
		return metaData;
	}

	public List<OdsView> getOdsViews() {
		return odsViews;
	}

}
