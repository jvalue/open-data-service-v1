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

import org.jvalue.ods.notifications.Client;
import org.jvalue.ods.notifications.ClientVisitor;
import org.jvalue.ods.utils.Assert;


public final class HttpClient extends Client {

	private final String restUrl, sourceParam;

	public HttpClient(String id, String source, String restUrl, String sourceParam) {
		super(id, source);
		Assert.assertNotNull(restUrl, sourceParam);
		this.restUrl = restUrl;
		this.sourceParam = sourceParam;
	}


	public String getRestUrl() {
		return restUrl;
	}


	public String getSourceParam() {
		return sourceParam;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof HttpClient)) return false;

		HttpClient client = (HttpClient) other;
		return client.restUrl.equals(restUrl) && client.sourceParam.equals(sourceParam);
	}


	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash + HASH_MULT * restUrl.hashCode();
		hash = hash + HASH_MULT * sourceParam.hashCode();
		return hash;
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
