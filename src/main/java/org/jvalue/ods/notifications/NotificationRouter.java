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
package org.jvalue.ods.notifications;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;



public final class NotificationRouter implements Router<Restlet> {

	private static final String
		ROUTE_REGISTER = "/notifications/register",
		ROUTE_UNREGISTER = "/notifications/unregister",
		ROUTE_DEBUG = "/notifications/debug";

	private static final String
		PARAM_REGID = "regId",
		PARAM_SOURCE = "source";

	private static final String
		MSG_BAD_REQUEST = "Usage: POST /<register|unregister>?" 
				+ PARAM_REGID + "=<id>&" + PARAM_SOURCE + "=<source>";


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();


		routes.put(ROUTE_REGISTER, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				if (!validateRequest(request, response)) return;

				String regId = getParameter(request, PARAM_REGID);
				String source = getParameter(request, PARAM_SOURCE);

				ClientDatastore.getInstance().registerClient(regId, source);
				Logging.info(
					NotificationRouter.class, 
					"Registered client " + regId + " for source " + source);
			}
		});


		routes.put(ROUTE_UNREGISTER, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				if (!validateRequest(request, response)) return;

				String regId = getParameter(request, PARAM_REGID);
				String source = getParameter(request, PARAM_SOURCE);

				ClientDatastore.getInstance().unregisterClient(regId, source);
				Logging.info(
					NotificationRouter.class, 
					"Unregistered client " + regId + " for source " + source);
			}
		});


		routes.put(ROUTE_DEBUG, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				try {
					String clients = new ObjectMapper().writeValueAsString(ClientDatastore.getInstance().getRegisteredClients());
					response.setEntity(clients, MediaType.APPLICATION_JSON);
				} catch (IOException io) {
					throw new RuntimeException(io);
				}
			}
		});


		return routes;
	}


	private static String getParameter(Request request, String key) {
		Parameter param = request.getResourceRef().getQueryAsForm().getFirst(key);
		if (param == null) return null;
		else return param.getValue();
	}


	private static boolean validateRequest(Request request, Response response) {
		boolean valid = true;

		if (getParameter(request, PARAM_REGID) == null 
				|| getParameter(request, PARAM_SOURCE) == null)
			valid = false;

		if (!request.getMethod().equals(Method.POST)) valid = false;

		if (!valid) response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, MSG_BAD_REQUEST);
		
		return valid;
	}

}
