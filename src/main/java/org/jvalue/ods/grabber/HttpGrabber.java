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
package org.jvalue.ods.grabber;

import java.io.IOException;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.HttpUtils;


final class HttpGrabber extends Grabber<String> {

	private final String encoding;

	public HttpGrabber(DataSource source, String encoding) {
		super(source);
		Assert.assertNotNull(encoding);
		this.encoding = encoding;
	}


	@Override
	public String grabSource() {
		try {
			return HttpUtils.readUrl(dataSource.getUrl(), encoding);
		} catch (IOException io) {
			Logging.error(this.getClass(), io.getMessage());
			return null;
		}
	}

}
