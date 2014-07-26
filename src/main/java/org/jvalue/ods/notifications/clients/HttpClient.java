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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public final class HttpClient extends Client {

	private final String restUrl, sourceParam;
	private final boolean sendData;

	@JsonCreator
	HttpClient(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("source") String source, 
			@JsonProperty("restUrl") String restUrl, 
			@JsonProperty("sourceParam") String sourceParam,
			@JsonProperty("sendData") boolean sendData) {

		super(clientId, source);
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
		if (!(other instanceof HttpClient)) return false;

		HttpClient client = (HttpClient) other;
		return client.restUrl.equals(restUrl) 
			&& client.sourceParam.equals(sourceParam) 
			&& client.sendData == sendData;
	}


	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash + HASH_MULT * restUrl.hashCode();
		hash = hash + HASH_MULT * sourceParam.hashCode();
		hash = hash + Boolean.valueOf(sendData).hashCode();
		return hash;
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
