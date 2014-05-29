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
package org.jvalue.ods.translator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.BoolObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.generic.NullObject;
import org.jvalue.ods.data.generic.NumberObject;
import org.jvalue.ods.data.generic.StringObject;
import org.jvalue.ods.data.schema.BoolSchema;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.grabber.Translator;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.schema.SchemaManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JsonTranslator.
 */
public class JsonTranslator implements Translator {

	/**
	 * Translate.
	 * 
	 * @param dataSource
	 *            the data source
	 * @return the generic value
	 */
	@Override
	public GenericEntity translate(DataSource dataSource) {
		if ((dataSource == null) || (dataSource.getUrl() == null))
			throw new IllegalArgumentException("source is null");

		HttpReader httpAdapter = new HttpReader(dataSource.getUrl());
		JsonNode rootNode = null;
		try {
			String json = httpAdapter.read("UTF-8");

			ObjectMapper mapper = new ObjectMapper();
			rootNode = mapper.readTree(json);

			if (dataSource.getDataSourceSchema() != null) {
				if (!validate(rootNode, dataSource.getDataSourceSchema())) {
					Logging.error(this.getClass(), "Could not validate source.");
					System.err.println("Could not validate source.");
					return null;
				}

				// if (schemaPath != null && schemaPath != "") {
				// validate(rootNode, schemaPath);
				// }
			}

		} catch (IOException e) {
			String error = "Could not grab source.";
			Logging.error(this.getClass(), error);
			System.err.println(error);
			return null;
		}

		GenericEntity gv = convertJson(rootNode);

		if (dataSource.getDataSourceSchema() != null) {
			if (!SchemaManager.validateGenericValusFitsSchema(gv,
					dataSource.getDataSourceSchema()))
				return null;
		}
		
		return gv;
	}

	/**
	 * Checks if is class of.
	 * 
	 * @param schema
	 *            the schema
	 * @param c
	 *            the c
	 * @return true, if is class of
	 */
	private boolean isClassOf(Schema schema, Class<?> c) {
		boolean result = schema.getClass().equals(c);
		if (result == false) {
			String error = "Validation error: Expected: "
					+ schema.getClass().getName() + " Actual: "
					+ c.getClass().getName();
			Logging.info(this.getClass(), error);
			System.err.println(error);
		}
		return result;
	}

	// Now: Validation, if schema fits json
	// ToDo: Validation, if json fits schema?
	/**
	 * Validate.
	 * 
	 * @param node
	 *            the node
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 */
	private boolean validate(JsonNode node, Schema schema) {
		if (node.isBoolean()) {
			if (!isClassOf(schema, BoolSchema.class)) {
				// not boolean
				return false;
			}
		} else if (node.isNull()) {
			if (!isClassOf(schema, NullSchema.class)) {
				// not null
				return false;
			}
		} else if (node.isTextual()) {
			if (!isClassOf(schema, StringSchema.class)) {
				// not string
				return false;
			}
		} else if (node.isArray()) {
			if (!isClassOf(schema, ListSchema.class)) {
				// not list
				return false;
			} else {
				return validateList(node, (ListSchema) schema);
			}
		} else if (node.isObject()) {
			if (!isClassOf(schema, MapSchema.class)) {
				// not map
				return false;
			} else {
				return validateMap(node, (MapSchema) schema);
			}
		}
		return true;
	}

	/**
	 * Validate map.
	 * 
	 * @param node
	 *            the node
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 */
	private boolean validateMap(JsonNode node, MapSchema schema) {
		Map<String, Schema> map = schema.getMap();

		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			if (map.containsKey(field.getKey())) {
				boolean result = validate(field.getValue(),
						map.get(field.getKey()));
				if (result == false)
					return false;
			} else {
				String error = "Validation error: Key for map not found: "
						+ field.getKey();
				Logging.info(this.getClass(), error);
				System.err.println(error);

				return false;
			}
		}
		return true;
	}

	/**
	 * Validate list.
	 * 
	 * @param node
	 *            the node
	 * @param schema
	 *            the schema
	 * @return true, if successful
	 */
	private boolean validateList(JsonNode node, ListSchema schema) {
		for (JsonNode n : node) {
			if (n.isBoolean()) {
				if (!containsClass(schema, BoolSchema.class)) {
					// expected schema not found
					return false;
				}
			} else if (n.isNull()) {
				if (!containsClass(schema, NullSchema.class)) {
					// expected schema not found
					return false;
				}
			} else if (n.isTextual()) {
				if (!containsClass(schema, StringSchema.class)) {
					// expected schema not found
					return false;
				}
			}

			else if (n.isArray()) {
				if (!containsClass(schema, ListSchema.class)) {
					// expected schema not found
					return false;
				} else {
					boolean result = findValidation(n, schema, ListSchema.class);
					if (result == false)
						return false;
				}
			} else if (n.isObject()) {
				if (!containsClass(schema, MapSchema.class)) {
					// expected schema not found
					return false;
				} else {
					boolean result = findValidation(n, schema, MapSchema.class);
					if (result == false)
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Find validation.
	 * 
	 * @param n
	 *            the n
	 * @param schema
	 *            the schema
	 * @param c
	 *            the c
	 * @return true, if successful
	 */
	private boolean findValidation(JsonNode n, ListSchema schema, Class<?> c) {
		for (Schema s : schema.getList()) {
			if (s.getClass().equals(c)) {
				boolean result = validate(n, s);
				if (result == true)
					return true;
			}
		}
		return false;
	}

	/**
	 * Contains class.
	 * 
	 * @param schema
	 *            the schema
	 * @param c
	 *            the c
	 * @return true, if successful
	 */
	private boolean containsClass(ListSchema schema, Class<?> c) {
		for (Schema s : schema.getList()) {
			if (s.getClass().equals(c))
				return true;
		}
		return false;
	}

	/**
	 * Convert json.
	 * 
	 * @param rootNode
	 *            the root node
	 * @return the generic value
	 */
	public GenericEntity convertJson(JsonNode rootNode) {

		GenericEntity gv = null;

		if (rootNode.isBoolean()) {
			gv = new BoolObject(rootNode.asBoolean());
		} else if (rootNode.isArray()) {
			gv = new ListObject();
			fillListRec(rootNode, (ListObject) gv);
		} else if (rootNode.isObject()) {
			gv = new MapObject();
			fillMapRec(rootNode, (MapObject) gv);
		} else if (rootNode.isNull()) {
			gv = new NullObject();
		} else if (rootNode.isNumber()) {
			gv = new NumberObject(rootNode.numberValue());
		} else if (rootNode.isTextual()) {
			gv = new StringObject(rootNode.asText());
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
	private void fillListRec(JsonNode node, ListObject lv) {
		for (JsonNode n : node) {
			GenericEntity gv = convertJson(n);
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
	private void fillMapRec(JsonNode node, MapObject mv) {

		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			GenericEntity gv = convertJson(field.getValue());
			mv.getMap().put(field.getKey(), gv);
		}
	}

	// private void validate(JsonNode rootNode, String schemaPath) {
	// JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	//
	// JsonSchema jsonSchema;
	//
	// try {
	//
	// JsonNode jn = JsonLoader.fromFile(new File(schemaPath));
	//
	// jsonSchema = factory.getJsonSchema(jn);
	// ProcessingReport report;
	// report = jsonSchema.validate(rootNode);
	// if (!report.isSuccess()) {
	// Logging.info(this.getClass(), "Validation error: " + report);
	// System.err.println("Validation error: " + report);
	// }
	//
	// } catch (ProcessingException e) {
	// Logging.error(this.getClass(), "Could not validate json: " + e);
	// System.err.println("Could not validate json" + e);
	// } catch (IOException e) {
	// Logging.error(this.getClass(), "Could not validate json: " + e);
	// System.err.println("Could not validate json" + e);
	// }
	// }
}
