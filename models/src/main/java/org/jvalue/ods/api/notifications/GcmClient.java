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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;


public final class GcmClient extends Client {

	static final String CLIENT_TYPE = "GCM";

	private final String gcmClientId;

	@JsonCreator
	public GcmClient(
			@JsonProperty("id") String id,
			@JsonProperty("gcmClientId") String gcmClientId) {

		super(id, CLIENT_TYPE);
		this.gcmClientId = gcmClientId;
	}

	
	public String getGcmClientId() {
		return gcmClientId;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		GcmClient client = (GcmClient) other;
		return Objects.equal(gcmClientId, client.gcmClientId);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), gcmClientId);
	}


	@Override
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
