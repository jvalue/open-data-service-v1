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

import org.jvalue.ods.notifications.RestAdapter;
import org.jvalue.ods.notifications.clients.RestClient;
import org.restlet.Request;


final class RestNotificationAdapter extends RestAdapter<RestClient> {

	private static final String
		PARAM_URL = "restUrl",
		PARAM_PARAM = "restParam";

	@Override
	protected RestClient toClient(Request request, String regId, String source) {
		String url = getParameter(request, PARAM_URL);
		String param = getParameter(request, PARAM_PARAM);
		return new RestClient(regId, source, url, param);
	}


	@Override
	protected void getParameters(Set<String> params) {
		params.add(PARAM_URL);
		params.add(PARAM_PARAM);
	}

}
