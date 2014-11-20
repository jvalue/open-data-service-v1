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
package org.jvalue.ods.filter.adapter;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.Log;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

final class FileSourceAdapter extends SourceAdapter<File> {

	@Inject
	FileSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public File grabSource() {
		File file = null;
		URL sourceUrl = getClass().getResource(dataSource.getUrl());
		try {
			file = new File(sourceUrl.toURI());
		} catch (URISyntaxException e) {
			Log.error(e.getMessage());
		}
		return file;
	}

}
