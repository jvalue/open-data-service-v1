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
package org.jvalue.ods.notifications.clients;

import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public abstract class Client {

	private final String clientId, source;

	protected Client(String clientId, String source) {
		Assert.assertNotNull(clientId, source);
		this.clientId = clientId;
		this.source = source;
	}


	public final String getClientId() {
		return clientId;
	}


	public final String getSource() {
		return source;
	}


	@Override
	public boolean equals(Object other) {
		return equalsIgnoreId(other) && ((Client) other).clientId.equals(clientId);
	}


	public boolean equalsIgnoreId(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return client.source.equals(source);
	}


	protected final static int HASH_MULT = 17;
	@Override
	public int hashCode() {
		int hash = 13;
		hash = hash + HASH_MULT * clientId.hashCode();
		hash = hash + HASH_MULT * source.hashCode();
		return hash;
	}


	public abstract <P,R> R accept(ClientVisitor<P,R> visitor, P param);

}
