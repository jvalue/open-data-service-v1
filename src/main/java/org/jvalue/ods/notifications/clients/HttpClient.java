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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;


public final class HttpClient extends Client {

	static final String CLIENT_TYPE = "HTTP";

	private final String restUrl, sourceParam;
	private final boolean sendData;

	@JsonCreator
	public HttpClient(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("restUrl") String restUrl,
			@JsonProperty("sourceParam") String sourceParam,
			@JsonProperty("sendData") boolean sendData) {

		super(clientId, CLIENT_TYPE);
		Assert.assertNotNull(restUrl, sourceParam);
		this.restUrl = restUrl;
		this.sourceParam = sourceParam;
		this.sendData = sendData;
	}


	public String getRestUrl() {
		return restUrl;
	}


	public String getSourceParam() {
		return sourceParam;
	}


	public boolean getSendData() {
		return sendData;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		HttpClient client = (HttpClient) other;
		return Objects.equal(restUrl, client.restUrl)
				&& Objects.equal(sourceParam, client.sourceParam)
				&& Objects.equal(sendData, client.sendData);
	}

	
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), restUrl, sourceParam, sendData);
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
