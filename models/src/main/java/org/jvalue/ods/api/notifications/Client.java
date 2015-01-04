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
package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;


@JsonTypeInfo(
		use = Id.NAME,
		include = As.PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = HttpClient.class, name = HttpClient.CLIENT_TYPE),
		@JsonSubTypes.Type(value = GcmClient.class, name = GcmClient.CLIENT_TYPE)
})
public abstract class Client {

	@NotNull private final String id, type;

	protected Client(String id, String type) {
		this.id = id;
		this.type = type;
	}


	public final String getId() {
		return id;
	}


	public final String getType() {
		return type;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return Objects.equal(id, client.id)
				&& Objects.equal(type, client.type);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, type);
	}


	public abstract <P,R> R accept(ClientVisitor<P,R> visitor, P param);

}
