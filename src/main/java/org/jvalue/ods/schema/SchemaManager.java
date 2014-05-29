/*
 * 
 */
package org.jvalue.ods.schema;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.schema.BoolSchema;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;
import com.github.fge.jsonschema.report.ProcessingReport;

/**
 * The Class SchemaManager.
 */
public class SchemaManager {

	/**
	 * Creates the json schema.
	 * 
	 * @param schema
	 *            the schema
	 * @return the string
	 */
	public static String createJsonSchema(Schema schema) {
		return createJsonSchema(schema, 0);
	}

	/**
	 * Creates the json schema.
	 * 
	 * @param schema
	 *            the schema
	 * @param tabs
	 *            the tabs
	 * @return the string
	 */
	private static String createJsonSchema(Schema schema, int tabs) {
		String result = "";

		if (schema.getClass().equals(MapSchema.class)) {
			MapSchema map = (MapSchema) schema;
			result = createJsonSchemaFromMap(map, tabs + 1);
		} else if (schema.getClass().equals(ListSchema.class)) {
			ListSchema list = (ListSchema) schema;
			result = createJsonSchemaFromList(list, tabs + 1);
		} else if (schema.getClass().equals(BoolSchema.class)) {
			result = createJsonSchemaFromElementary("boolean", tabs + 1);
		} else if (schema.getClass().equals(NullSchema.class)) {
			result = createJsonSchemaFromElementary("null", tabs + 1);
		} else if (schema.getClass().equals(NumberSchema.class)) {
			result = createJsonSchemaFromElementary("number", tabs + 1);
		} else if (schema.getClass().equals(StringSchema.class)) {
			result = createJsonSchemaFromElementary("string", tabs + 1);
		}

		return result;
	}

	/**
	 * Creates the tabs.
	 * 
	 * @param tabCount
	 *            the tab count
	 * @return the string
	 */
	private static String createTabs(int tabCount) {
		String s = "";

		for (int i = 0; i < tabCount; i++)
			s += "\t";

		return s;
	}

	/**
	 * Creates the json schema from elementary.
	 * 
	 * @param type
	 *            the type
	 * @param tabs
	 *            the tabs
	 * @return the string
	 */
	private static String createJsonSchemaFromElementary(String type, int tabs) {
		/*
		 * example: { "type" : "string" }
		 */
		String result;
		result = "{" + "\n";
		result += createTabs(tabs) + "\"type\": \"" + type + "\"" + "\n";
		result += createTabs(tabs - 1) + "}";
		return result;
	}

	/**
	 * Creates the json schema from list.
	 * 
	 * @param listSchema
	 *            the list schema
	 * @param tabs
	 *            the tabs
	 * @return the string
	 */
	private static String createJsonSchemaFromList(ListSchema listSchema,
			int tabs) {
		/*
		 * example: { "type": "array", "items": { ... } }
		 */
		String result = "{" + "\n";
		result += createTabs(tabs) + "\"type\": \"array\"," + "\n";
		result += createTabs(tabs) + "\"items\": ";

		List<Schema> list = listSchema.getList();
		for (int i = 0; i < list.size(); i++) {
			Schema schema = list.get(i);

			if (i != list.size() - 1) {
				result += createJsonSchema(schema, tabs) + "," + "\n";
			} else {
				result += createJsonSchema(schema, tabs) + "\n";
			}
		}

		result += createTabs(tabs - 1) + "}";

		return result;
	}

	/**
	 * Creates the json schema from map.
	 * 
	 * @param mapSchema
	 *            the map schema
	 * @param tabs
	 *            the tabs
	 * @return the string
	 */
	@SuppressWarnings("unchecked")
	private static String createJsonSchemaFromMap(MapSchema mapSchema, int tabs) {
		/*
		 * { "type": "object", "properties": { ... } }
		 */

		String result = "{" + "\n";
		result += createTabs(tabs) + "\"type\": \"object\"," + "\n";
		result += createTabs(tabs) + "\"properties\": {" + "\n";

		Map<String, Schema> map = mapSchema.getMap();
		Object[] arr = map.entrySet().toArray();
		for (int i = 0; i < arr.length; i++) {
			Entry<String, Schema> entry = (Entry<String, Schema>) arr[i];
			if (i != arr.length - 1) {
				result += createTabs(tabs + 1) + "\"" + entry.getKey() + "\": "
						+ createJsonSchema(entry.getValue(), tabs + 1) + ","
						+ "\n";
			} else {
				result += createTabs(tabs + 1) + "\"" + entry.getKey() + "\": "
						+ createJsonSchema(entry.getValue(), tabs + 1) + "\n";
			}
		}

		result += createTabs(tabs) + "}" + "\n";
		result += createTabs(tabs - 1) + "}";

		return result;
	}

	/**
	 * Validate generic valus fits schema.
	 * 
	 * @param gv
	 *            the gv
	 * @param dbSchema
	 *            the db schema
	 * @return true, if successful
	 */
	public static boolean validateGenericValusFitsSchema(GenericEntity gv,
			Schema dbSchema) {
		ObjectMapper mapper = new ObjectMapper();

		String json;
		try {
			json = mapper.writeValueAsString(gv);
		} catch (JsonProcessingException e) {
			String error = "Could not convert GenericValue to json"
					+ e.getMessage();
			Logging.error(SchemaManager.class, error);
			System.err.println(error);
			return false;
		}

		try {
			JsonNode jsonNode = mapper.readTree(json);
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			String jsonSchema = createJsonSchema(dbSchema);

			// validate jsonSchema
			JsonNode jn = JsonLoader.fromString(jsonSchema);
			SyntaxValidator validator = factory.getSyntaxValidator();
			boolean result = validator.schemaIsValid(jn);
			if (result == false) {
				String error = "schema is not valid";
				Logging.error(SchemaManager.class, error);
				System.err.println(error);
				return false;
			}

			// validate
			JsonValidator jsonValidator = factory.getValidator();
			ProcessingReport report = jsonValidator.validate(jn, jsonNode);
			result = report.isSuccess();
			if (result == false) {
				String error = "Could not validate json: " + report;
				Logging.error(SchemaManager.class, error);
				System.err.println(error);
				return false;
			}

		} catch (IOException e) {
			String error = "Could not validate json" + e.getMessage();
			Logging.error(SchemaManager.class, error);
			System.err.println(error);
			return false;
		} catch (ProcessingException e) {
			String error = "Could not validate json" + e.getMessage();
			Logging.error(SchemaManager.class, error);
			System.err.println(error);
			return false;
		}
		return true;
	}

}
