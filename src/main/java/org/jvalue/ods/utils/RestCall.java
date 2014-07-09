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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class RestCall {

	public enum RequestType {
		GET("GET"),
		POST("POST"),
		UPDATE("UPDATE"),
		DELETE("DELETE");

		private final String type;
		RequestType(String type) { this.type = type; }

		@Override
		public String toString() { return type; }
	}


	private final RequestType requestType;
	private final String baseUrl;
	private final List<String> paths;
	private final Map<String, String> parameters;
	private final Map<String, String> headers;
	private final String contentType;
	private final byte[] content;


	private RestCall(
				RequestType requestType,
				String baseUrl,
				List<String> paths,
				Map<String, String> parameters,
				Map<String, String> headers,
				String contentType,
				byte[] content) {
		
		this.requestType = requestType;
		this.baseUrl = baseUrl;
		this.paths = paths;
		this.parameters = parameters;
		this.headers = headers;
		this.contentType = contentType;
		this.content = content;
	}


	public String execute() throws RestException {

		StringBuilder urlBuilder = new StringBuilder(baseUrl);

		// format paths
		for (String path : paths) urlBuilder.append("/" + path);

		// format params
		if (parameters.size() != 0) {
			urlBuilder.append("?");
			boolean firstKey = true;
			for (String key : parameters.keySet()) {
				if (!firstKey) urlBuilder.append("&");
				firstKey = false;

				urlBuilder.append(key + "=" + parameters.get(key));
			}
		}


		// create connection
		HttpURLConnection conn = null;
		OutputStream outputStream = null;
		BufferedReader dataReader = null;
		try {
			URL url = new URL(urlBuilder.toString());

			conn = (HttpURLConnection) url.openConnection();
			for (String key : headers.keySet()) {
				conn.setRequestProperty(key, headers.get(key));
			}
			conn.setUseCaches(false);
			conn.setRequestMethod(requestType.toString());

			if (contentType != null) {
				conn.setDoOutput(true);

				conn.setRequestProperty("Content-Type", contentType);
				conn.setRequestProperty("Content-Length", Integer.toString(content.length));

				outputStream = conn.getOutputStream();
				outputStream.write(content);
				outputStream.flush();
			}


			int responseCode = conn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_CREATED
					&& responseCode != HttpURLConnection.HTTP_OK
					&& responseCode != HttpURLConnection.HTTP_ACCEPTED)
				throw new RestException(responseCode);

			dataReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder dataBuilder = new StringBuilder();
			String line;
			while ((line = dataReader.readLine()) != null)
				dataBuilder.append(line);

			return dataBuilder.toString();
			
		} catch (IOException ie) {
			throw new RestException(ie);

		} finally {
			try {
				if (outputStream != null) outputStream.close();
				if (dataReader != null) dataReader.close();
			} catch (IOException e) { }

			if (conn != null) conn.disconnect();
		}
	}

	

	public static class Builder {
		private final RequestType requestType;
		private final String baseUrl;
		private final List<String> paths = new LinkedList<String>();
		private final Map<String, String> parameters = new HashMap<String, String>();
		private final Map<String, String> headers = new HashMap<String, String>();
		private byte[] content = null;
		private String contentType = null;

		private boolean built = false;

		public Builder(final RequestType requestType, final String baseUrl) {
			Assert.assertNotNull(requestType, baseUrl);
			this.requestType = requestType;
			this.baseUrl = baseUrl;
		}

		public Builder path(String path) {
			Assert.assertNotNull(path);
			Assert.assertFalse(built, "already built");
			paths.add(path);
			return this;
		}

		public Builder parameter(String key, String value) {
			Assert.assertNotNull(key, value);
			Assert.assertFalse(built, "already built");
			parameters.put(key, value);
			return this;
		}

		public Builder header(String key, String value) {
			Assert.assertNotNull(key, value);
			Assert.assertFalse(built, "already built");
			headers.put(key, value);
			return this;
		}

		public Builder content(String contentType, byte[] content) {
			Assert.assertNotNull(contentType, content);
			Assert.assertFalse(built, "already built");
			this.contentType = contentType;
			this.content = content;
			return this;
		}


		public RestCall build() {
			Assert.assertFalse(built, "already built");
			built = true;
			return new RestCall(requestType, baseUrl, paths, parameters, headers, contentType, content);
		}
	}
}
