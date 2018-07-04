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

import javax.validation.constraints.NotNull;


public final class HttpClient extends Client {

	public static final String CLIENT_TYPE = "HTTP";

	@NotNull private final String callbackUrl;
	private final boolean sendData;

	@JsonCreator
	public HttpClient(
			@JsonProperty("id") String id,
			@JsonProperty("callbackUrl") String callbackUrl,
			@JsonProperty("sendData") boolean sendData) {

		super(id, CLIENT_TYPE);
		this.callbackUrl = callbackUrl;
		this.sendData = sendData;
	}


	public String getCallbackUrl() {
		return callbackUrl;
	}


	public boolean getSendData() {
		return sendData;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		HttpClient client = (HttpClient) other;
		return Objects.equal(callbackUrl, client.callbackUrl)
				&& Objects.equal(sendData, client.sendData);
	}

	
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), callbackUrl, sendData);
	}


	@Override
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
