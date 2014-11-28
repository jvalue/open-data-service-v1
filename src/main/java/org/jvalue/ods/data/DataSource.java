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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Objects;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.JsonPointerDeserializer;
import org.jvalue.ods.utils.JsonPointerSerializer;

import java.net.URL;

public final class DataSource extends CouchDbDocument {

	private final String sourceId;
	private final URL url;
	private final DataSourceMetaData metaData;
	@JsonSerialize(using = JsonPointerSerializer.class)
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	private final JsonPointer domainIdKey;
	private final ObjectNode schema;

	// TODO tmp fix as schema are currently not JSON capable
	private final ObjectType rawDbSchema = null;
	private final ObjectType improvedDbSchema = null;


	@JsonCreator
	public DataSource(
			@JsonProperty("sourceId") String sourceId,
			@JsonProperty("url") URL url,
			@JsonProperty("domainIdKey") JsonPointer domainIdKey,
			@JsonProperty("schema") ObjectNode schema,
			@JsonProperty("metaData") DataSourceMetaData metaData) {

		Assert.assertNotNull(sourceId, url, domainIdKey, schema, metaData);

		this.sourceId = sourceId;
		this.url = url;
		this.domainIdKey = domainIdKey;
		this.schema = schema;
		this.metaData = metaData;
	}


	public String getSourceId() {
		return sourceId;
	}


	public URL getUrl() {
		return url;
	}


	public ObjectNode getSchema() {
		return schema;
	}


	public ObjectType getImprovedDbSchema() {
		return improvedDbSchema;
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
				&& Objects.equal(schema, source.schema)
				// && Objects.equal(rawDbSchema, source.rawDbSchema)
				// && Objects.equal(improvedDbSchema, source.improvedDbSchema)
				&& Objects.equal(metaData, source.metaData)
				&& Objects.equal(domainIdKey, source.domainIdKey);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sourceId, url, schema, metaData, domainIdKey);
	}

}
