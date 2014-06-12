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
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.jvalue.ods.data.DataSourceManager;
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
		ROUTE_DEBUG_CLIENTS = "/notifications/debug/clients",
		ROUTE_DEBUG_TEST_MESSAGE = "/notifications/debug/testMessage",
		ROUTE_DEBUG_SOURCES = "/notifications/debug/sources";

	private static final String
		PARAM_REGID = "regId",
		PARAM_SOURCE = "source";

	private static final String MSG_BAD_REQUEST = "Usage: POST /<url>?";


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();


		routes.put(ROUTE_REGISTER, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				if (!validateRequest(
						request, 
						response, 
						new String[] { PARAM_REGID, PARAM_SOURCE })) 
					return;

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
				if (!validateRequest(
						request, 
						response, 
						new String[] { PARAM_REGID, PARAM_SOURCE })) 
					return;

				String regId = getParameter(request, PARAM_REGID);
				String source = getParameter(request, PARAM_SOURCE);

				ClientDatastore.getInstance().unregisterClient(regId, source);
				Logging.info(
					NotificationRouter.class, 
					"Unregistered client " + regId + " for source " + source);
			}
		});


		routes.put(ROUTE_DEBUG_CLIENTS, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				try {
					String clients = new ObjectMapper().writeValueAsString(
						ClientDatastore.getInstance().getRegisteredClients());
					response.setEntity(clients, MediaType.APPLICATION_JSON);
				} catch (IOException io) {
					throw new RuntimeException(io);
				}
			}
		});


		routes.put(ROUTE_DEBUG_TEST_MESSAGE, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				if (!validateRequest(
						request, 
						response, 
						new String[] { PARAM_SOURCE })) 
					return;

				String source = getParameter(request, PARAM_SOURCE);

				Map<String,String> payload = new HashMap<String,String>();
				payload.put(
					NotificationSender.DATA_KEY_SOURCE, 
					source);
				payload.put(
					NotificationSender.DATA_KEY_DEBUG,
					Boolean.TRUE.toString());
				String collapsKey = NotificationSender.DATA_KEY_DEBUG;

				Set<String> clients = ClientDatastore
					.getInstance()
					.getRegisteredClients()
					.get(source);
				if (clients == null) return;

				NotificationSender.getInstance().sendNotification(clients, payload, collapsKey);
			}
		});


		routes.put(ROUTE_DEBUG_SOURCES, new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				try {
					String sources = new ObjectMapper().writeValueAsString(
						DataSourceManager.getInstance().getAllIds());
					response.setEntity(sources, MediaType.APPLICATION_JSON);
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


	private static boolean validateRequest(
			Request request, 
			Response response,
			String[] requiredParams) {

		boolean valid = true;

		for (String param : requiredParams) {
			if (getParameter(request, param) == null) valid = false;
		}

		if (!request.getMethod().equals(Method.POST)) valid = false;

		boolean first = true;
		String msg = MSG_BAD_REQUEST;
		for (String param : requiredParams) {
			if (first) first = false;
			else msg = msg + "&";
			msg = msg + param + "=<value>";
		}
		if (!valid) response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, msg);
		return valid;
	}

}
