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
package org.jvalue.ods.grabber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;

import org.jvalue.ods.logger.Logging;

/**
 * The Class HttpJsonReader.
 */
public class HttpJsonReader {

	/** The url. */
	private String url;

	/**
	 * Instantiates a new http json adapter.
	 * 
	 * @param url
	 *            the url
	 */
	public HttpJsonReader(String url) {
		if (url == null || url.isEmpty()) {
			String errorMessage = "url is null or empty";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		this.url = url;
	}

	/**
	 * Gets the json.
	 * 
	 * @param charsetName
	 *            the charset name
	 * @return the json
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getJSON(String charsetName) throws IOException {
		// using string builder for best performance of string concatenation
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		BufferedReader rd = null;
		try {

			URL url = new URL(this.url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);

			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), Charset.forName(charsetName)));
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
		} catch (SocketTimeoutException ste) {
			String errorMessage = "socket timeout";
			Logging.error(this.getClass(), errorMessage);
			throw new IOException(errorMessage);
		} catch (IOException ioe) {
			String errorMessage = "An I/O Exception occured while trying to read from HTTP server.";
			Logging.error(this.getClass(), errorMessage);
			throw new IOException(errorMessage);
		} finally {
			// close stream and connection
			try {
				if (rd != null) {
					rd.close();
				}
			} catch (IOException e) {
				String errorMessage = "An I/O Exception occured while trying to read from HTTP server.";
				Logging.error(this.getClass(), errorMessage);
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}
		return sb.toString();
	}
}