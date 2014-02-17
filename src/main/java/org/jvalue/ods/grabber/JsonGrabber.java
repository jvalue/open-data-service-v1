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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jvalue.ods.data.BoolValue;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.ListValue;
import org.jvalue.ods.data.MapValue;
import org.jvalue.ods.data.NullValue;
import org.jvalue.ods.data.NumberValue;
import org.jvalue.ods.data.StringValue;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;

/**
 * The Class JsonGrabber.
 */
public class JsonGrabber {

	/**
	 * Grab.
	 * 
	 * @param source
	 *            the source
	 * @return the generic value
	 */
	public GenericValue grab(String source, String schemaPath) {
		if (source == null)
			throw new IllegalArgumentException("source is null");

		HttpReader httpAdapter = new HttpReader(source);
		JsonNode rootNode = null;
		try {
			String json = httpAdapter.read("UTF-8");

			ObjectMapper mapper = new ObjectMapper();
			rootNode = mapper.readTree(json);

			if (schemaPath != null && schemaPath != "") {
				validate(rootNode, schemaPath);
			}

		} catch (IOException e) {
			Logging.error(this.getClass(), "Could not grab source.");
			System.err.println("Could not grab source.");
			return null;
		}

		GenericValue gv = convertJson(rootNode);

		return gv;

	}

	private void validate(JsonNode rootNode, String schemaPath) {
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

		JsonSchema jsonSchema;

		try {

			JsonNode jn = JsonLoader.fromFile(new File(schemaPath));

			jsonSchema = factory.getJsonSchema(jn);
			ProcessingReport report;
			report = jsonSchema.validate(rootNode);
			if (!report.isSuccess()) {
				Logging.info(this.getClass(), "Validation error: " + report);
				System.err.println("Validation error: " + report);
			}

		} catch (ProcessingException e) {
			Logging.error(this.getClass(), "Could not validate json: " + e);
			System.err.println("Could not validate json" + e);
		} catch (IOException e) {
			Logging.error(this.getClass(), "Could not validate json: " + e);
			System.err.println("Could not validate json" + e);
		}

	}

	/**
	 * Convert json.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the generic value
	 */
	private GenericValue convertJson(JsonNode rootNode) {

		GenericValue gv = null;

		if (rootNode.isBoolean()) {
			gv = new BoolValue(rootNode.asBoolean());
		} else if (rootNode.isArray()) {
			gv = new ListValue();
			fillListRec(rootNode, (ListValue) gv);
		} else if (rootNode.isObject()) {
			gv = new MapValue();
			fillMapRec(rootNode, (MapValue) gv);
		} else if (rootNode.isNull()) {
			gv = new NullValue();
		} else if (rootNode.isNumber()) {
			gv = new NumberValue(rootNode.numberValue());
		} else if (rootNode.isTextual()) {
			gv = new StringValue(rootNode.asText());
		}

		return gv;
	}

	/**
	 * Fill list rec.
	 * 
	 * @param node
	 *            the node
	 * @param lv
	 *            the lv
	 */
	private void fillListRec(JsonNode node, ListValue lv) {

		for (JsonNode n : node) {
			GenericValue gv = convertJson(n);
			lv.getList().add(gv);
		}

	}

	/**
	 * Fill map rec.
	 * 
	 * @param node
	 *            the node
	 * @param mv
	 *            the mv
	 */
	private void fillMapRec(JsonNode node, MapValue mv) {

		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			GenericValue gv = convertJson(field.getValue());
			mv.getMap().put(field.getKey(), gv);
		}

	}

}
