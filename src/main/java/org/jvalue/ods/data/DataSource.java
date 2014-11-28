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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.google.common.base.Objects;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.utils.Assert;

public final class DataSource extends CouchDbDocument {

	private final String sourceId;
	private final String url;
	private final DataSourceMetaData metaData;
	private final JsonPointer domainIdKey;

	// TODO tmp fix as schema are currently not JSON capable
	private final ObjectType sourceSchema = null;
	private final ObjectType rawDbSchema = null;
	private final ObjectType improvedDbSchema = null;


	@JsonCreator
	public DataSource(
			@JsonProperty("sourceId") String sourceId,
			@JsonProperty("url") String url,
			// @JsonProperty("sourceSchema") ObjectType sourceSchema,
			// @JsonProperty("rawDbSchema") MapObjectType rawDbSchema,
			// @JsonProperty("improvedDbSchema") MapObjectType improvedDbSchema,
			@JsonProperty("metaData") DataSourceMetaData metaData,
			@JsonProperty("domainIdKey") JsonPointer domainIdKey) {

		Assert.assertNotNull(sourceId, url, metaData, domainIdKey);

		this.sourceId = sourceId;
		this.url = url;
		// this.sourceSchema = sourceSchema;
		// this.rawDbSchema = rawDbSchema;
		// this.improvedDbSchema = improvedDbSchema;
		this.metaData = metaData;
		this.domainIdKey = domainIdKey;
	}


	public String getSourceId() {
		return sourceId;
	}


	public String getUrl() {
		return url;
	}


	public ObjectType getSourceSchema() {
		return sourceSchema;
	}


	public ObjectType getImprovedDbSchema() {
		return improvedDbSchema;
	}


	public ObjectType getRawDbSchema() {
		return rawDbSchema;
	}


	public DataSourceMetaData getMetaData() {
		return metaData;
	}


	public JsonPointer getDomainIdKey() {
		return domainIdKey;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		if (other == this) return true;
		DataSource source = (DataSource) other;
		return Objects.equal(sourceId, source.sourceId)
				&& Objects.equal(url, source.url)
				&& Objects.equal(sourceSchema, source.sourceSchema)
				&& Objects.equal(rawDbSchema, source.rawDbSchema)
				&& Objects.equal(improvedDbSchema, source.improvedDbSchema)
				&& Objects.equal(metaData, source.metaData)
				&& Objects.equal(domainIdKey, source.domainIdKey);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sourceId, url, sourceSchema, rawDbSchema, improvedDbSchema, metaData, domainIdKey);
	}

}
