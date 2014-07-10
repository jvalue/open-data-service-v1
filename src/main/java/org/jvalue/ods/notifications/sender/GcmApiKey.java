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
package org.jvalue.ods.notifications.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.jvalue.ods.logger.Logging;


final class GcmApiKey {

	private static GcmApiKey instance = null;
	public static GcmApiKey getInstance() throws NotificationException {
		if (instance == null) instance = new GcmApiKey();
		return instance;
	}


	private static String resourceName = "/googleApi.key";
	private final String key;

	private GcmApiKey() throws NotificationException {
		URL resourceUrl = getClass().getResource(resourceName);

		String key = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(resourceUrl.toURI())));
			key = reader.readLine();
			if (reader.readLine() != null) Logging.info(
					getClass(), 
					"ApiKey contains more than one line!");

		} catch(Exception e) {
			throw new NotificationException(e);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch(IOException ioc) {
				Logging.error(getClass(), ioc.getMessage());
			}
			this.key = key;
		}
	}


	@Override
	public String toString() {
		return key;
	}


	/**
	 * Used for testing, as the real key  might not be present on some builds.
	 * Has to be called before first getInstance to function.
	 */
	static void setKeyResourceName(String resourceName) {
		GcmApiKey.resourceName = resourceName;
	}

}
