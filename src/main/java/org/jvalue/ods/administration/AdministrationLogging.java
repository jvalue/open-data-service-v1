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
package org.jvalue.ods.administration;

public final class AdministrationLogging {

	private AdministrationLogging() { }

	public static void log(String content) {
		System.out.println(content);
		// TODO
		/*
		try {
			DbAccessor<JsonNode> accessor = DbFactory
					.createDbAccessor("adminlog");
			accessor.connect();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String datetime = dateFormat.format(new Date());

			if (!content.endsWith("\n"))
				content += "\n";

			ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
			node.put("log", datetime + " " + content);
			accessor.insert(node);

		} catch (Exception ex) {
			Log.error(ex.getMessage());
		}
		*/
	}

}
