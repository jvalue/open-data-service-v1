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
package org.jvalue.ods.utils;

import java.io.IOException;

import org.restlet.data.Status;


public final class RestException extends Exception {
	public static final long serialVersionUID = 42L;

	private final Status status;

	public RestException(Status status) {
		Assert.assertNotNull(status);
		this.status = status;
	}

	public RestException(IOException nestedException) {
		super(nestedException);
		Assert.assertNotNull(nestedException);
		this.status = null;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		Throwable cause = getCause();
		if (cause != null) return "Exception while communicating with server (" + cause.getMessage() + ")";
		
		if (status != null) return status.toString();
		return "internal error in RestException";
	}
}
