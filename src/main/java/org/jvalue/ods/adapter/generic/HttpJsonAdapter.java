/*
    Open Data Service
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
package org.jvalue.ods.adapter.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * The Class HttpJsonAdapter.
 */
public class HttpJsonAdapter {

	/** The url. */
	private String url;

	/**
	 * Instantiates a new http json adapter.
	 * 
	 * @param url
	 *            the url
	 */
	public HttpJsonAdapter(String url) {
		this.url = url;
	}

	/**
	 * Gets the request.
	 *
	 * @param charsetName the charset name
	 * @return the request
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getRequest(String charsetName) throws IOException {
		// using string builder for best performance of string concatenation
		StringBuilder sb = new StringBuilder();
		
		URL url = new URL(this.url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
			
		BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), Charset.forName(charsetName)));
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		
		return sb.toString();
	}

	/**
	 * Gets the json.
	 *
	 * @param charsetName the charset name
	 * @return the json
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getJSON(String charsetName) throws IOException {
		return getRequest(charsetName);
	}
}