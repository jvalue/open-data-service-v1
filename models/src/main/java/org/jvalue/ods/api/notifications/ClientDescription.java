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
		@JsonSubTypes.Type(value = HttpClientDescription.class, name = HttpClient.CLIENT_TYPE),
		@JsonSubTypes.Type(value = GcmClientDescription.class, name = GcmClient.CLIENT_TYPE),
		@JsonSubTypes.Type(value = AmqpClientDescription.class, name = AmqpClient.CLIENT_TYPE)
})
public abstract class ClientDescription {

	@NotNull private final String type;

	protected ClientDescription(String type) {
		this.type = type;
	}


	public final String getType() {
		return type;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ClientDescription)) return false;
		if (other == this) return true;
		ClientDescription client = (ClientDescription) other;
		return Objects.equal(type, client.type);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(type);
	}


	public abstract <P,R> R accept(ClientDescriptionVisitor<P,R> visitor, P param);

}
