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
import java.net.HttpURLConnection;


public final class RestException extends Exception {
	public static final long serialVersionUID = 42L;

	public static final int UNSET  = -1;
	private final int code;

	public RestException(int code) {
		this.code = code;
	}

	public RestException(IOException nestedException) {
		super(nestedException);
		this.code = RestException.UNSET;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		Throwable cause = getCause();
		if (cause != null) return "Failed to communicate with servers (" + cause.getMessage() + ")";
		
		if (code == HttpURLConnection.HTTP_NOT_MODIFIED) {
			return "The requested documents were not modified";
		} else if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
			return "The servers did not understand the request";
		} else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
			return "The credentials provided were not correct";
		} else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
			return "The request is not allowed to make these changes";
		} else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
			return "The requested resource is not available";
		} else if (code >= 500 && code <= 600) {
			return "Unable to connect to the servers";
		}
		return "Unknown error (" + code + ")";
	}
}
