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

import org.junit.Test;


public class HttpUtilsTest {

	final String testUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/waters.json?includeStations=true&includeTimeseries=true&includeCurrentMeasurement=true";


	@Test(expected = NullPointerException.class)
	public void testHttpJsonReaderWithNullUrl() throws IOException {
		HttpUtils.readUrl(null, null);
	}


	@Test(expected = IOException.class)
	public void testHttpJsonReaderWithInvalidUrl() throws IOException {
		HttpUtils.readUrl("invalidUrl", "UTF-8");
	}


	@Test()
	public void testGetJSON() throws IOException {
		HttpUtils.readUrl(testUrl, "UTF-8");
	}

}
