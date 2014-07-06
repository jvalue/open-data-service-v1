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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.restlet.Request;
import org.restlet.data.Parameter;


public abstract class RestAdapter<C extends Client> {

	private static final String
		PARAM_REGID = "regId",
		PARAM_SOURCE = "source";

	private Set<String> parameters;


	public final C toClient(Request request) {
		String regId = getParameter(request, PARAM_REGID);
		String source = getParameter(request, PARAM_SOURCE);
		return toClient(request, regId, source);
	}


	public Set<String> getParameters() {
		if (parameters != null) return parameters;

		Set<String> parameters = new HashSet<String>();
		parameters.add(PARAM_REGID);
		parameters.add(PARAM_SOURCE);
		getParameters(parameters);
		this.parameters = Collections.unmodifiableSet(parameters);

		return this.parameters;
	}


	protected String getParameter(Request request, String key) {
		Parameter param = request.getResourceRef().getQueryAsForm().getFirst(key);
		if (param == null) return null;
		else return param.getValue();
	}


	protected abstract C toClient(Request request, String regId, String source);
	protected abstract void getParameters(Set<String> params);

}
