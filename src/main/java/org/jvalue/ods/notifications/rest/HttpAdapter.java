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
package org.jvalue.ods.notifications.rest;

import java.util.Set;

import org.jvalue.ods.notifications.clients.HttpClient;
import org.restlet.Request;


final class HttpAdapter extends RestAdapter<HttpClient> {

	private static final String
		PARAM_URL = "restUrl",
		PARAM_PARAM = "restParam",
		PARAM_SEND_DATA = "sendData";

	@Override
	protected HttpClient toClient(Request request, String regId, String source) {
		String url = getParameter(request, PARAM_URL);
		String param = getParameter(request, PARAM_PARAM);
		boolean sendData = Boolean.valueOf(getParameter(request, PARAM_SEND_DATA));
		return new HttpClient(regId, source, url, param, sendData);
	}


	@Override
	protected void getParameters(Set<String> params) {
		params.add(PARAM_URL);
		params.add(PARAM_PARAM);
		params.add(PARAM_SEND_DATA);
	}

}
