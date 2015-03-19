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
package org.jvalue.ods.utils;

import org.jvalue.common.utils.Assert;
import org.jvalue.common.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class HttpUtils {

	private HttpUtils () { } 

	public static String readUrl(URL url, String charsetName) throws IOException {
		Assert.assertNotNull(url, charsetName);

		BufferedReader reader = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),
					Charset.forName(charsetName)));

			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			return stringBuilder.toString();

		} finally {
			try {
				if (reader != null)  reader.close();
			} catch (IOException e) {
				Log.error("Failed to close reader", e);
			}
		}
	}

}
