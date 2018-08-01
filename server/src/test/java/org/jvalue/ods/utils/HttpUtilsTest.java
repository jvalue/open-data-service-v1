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

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;


public class HttpUtilsTest {

	final String testUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/waters.json?includeStations=true&includeTimeseries=true&includeCurrentMeasurement=true";
	@Mocked
	private UriInfo uriInfo;

	@Test(expected = NullPointerException.class)
	public void testHttpJsonReaderWithNullUrl() throws IOException {
		HttpUtils.readUrl(null, null);
	}


	@Test()
	public void testGetJSON() throws IOException {
		HttpUtils.readUrl(new URL(testUrl), "UTF-8");
	}


	@Test
	public void testAppendTrailingSlash() {
		URI withTrailingSlash = URI.create("http://www.pegelonline.wsv.de/webservices/rest-api/v2/");
		URI withoutTrailingSlash = URI.create("http://www.pegelonline.wsv.de/webservices/rest-api/v2");

		URI resultUnchanged = HttpUtils.appendTrailingSlash(withTrailingSlash);
		URI resultSlashAdded = HttpUtils.appendTrailingSlash(withoutTrailingSlash);

		Assert.assertEquals(withTrailingSlash, resultUnchanged);
		Assert.assertEquals(withTrailingSlash, resultSlashAdded);
	}


	@Test
	public void testGetSanitizedPath() {
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create("http://www.pegelonline.wsv.de/webservices/rest-api/v2");
		}};

		URI result = getSanitizedPath(uriInfo);

		Assert.assertEquals("http://www.pegelonline.wsv.de/webservices/rest-api/v2/", result.toString());
	}


	@Test
	public void testGetDirectoryURI() {
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create("http://www.pegelonline.wsv.de/webservices/rest-api/v2");
		}};

		URI result = getDirectoryURI(uriInfo);

		Assert.assertEquals("http://www.pegelonline.wsv.de/webservices/rest-api/", result.toString());
	}

}
