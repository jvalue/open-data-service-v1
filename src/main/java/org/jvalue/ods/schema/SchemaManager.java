/*
 * 
 */
package org.jvalue.ods.schema;

import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.objecttypes.ObjectType;

/**
 * The Class SchemaManager.
 */
public class SchemaManager {
//
//	/**
//	 * Creates the json schema.
//	 * 
//	 * @param listObjectType
//	 *            the schema
//	 * @return the string
//	 */
//	public static String createJsonSchema(ObjectType objectType) {
//		return createJsonSchema(objectType, 0);
//	}
//
//	/**
//	 * Creates the json schema.
//	 * 
//	 * @param objectType
//	 *            the schema
//	 * @param tabs
//	 *            the tabs
//	 * @return the string
//	 */
//	private static String createJsonSchema(ObjectType objectType, int tabs) {
//		String result = "";
//
//		if (objectType.getClass().equals(MapComplexValueType.class)) {
//			MapComplexValueType map = (MapComplexValueType) objectType;
//			result = createJsonSchemaFromMap(map, tabs + 1);
//		} else if (objectType.getClass().equals(ListComplexValueType.class)) {
//			ListComplexValueType list = (ListComplexValueType) objectType;
//			result = createJsonSchemaFromList(list, tabs + 1);
//		} else if (objectType.getClass().equals(SimpleValueType.class)) {
//			SimpleValueType bot = (SimpleValueType) objectType;			
//			if (bot.getName() == "java.lang.Boolean")
//			{
//				result = createJsonSchemaFromElementary("boolean", tabs + 1);
//			}
//			else if (bot.getName() == "Null") 
//			{
//				result = createJsonSchemaFromElementary("null", tabs + 1);
//			}
//			else if (bot.getName() == "java.lang.Number") 
//			{
//				result = createJsonSchemaFromElementary("number", tabs + 1);
//			}
//			else if (bot.getName() == "java.lang.String")
//			{
//				result = createJsonSchemaFromElementary("string", tabs + 1);
//			}
//			
//		}
//
//		return result;
//	}
//
//	/**
//	 * Creates the tabs.
//	 * 
//	 * @param tabCount
//	 *            the tab count
//	 * @return the string
//	 */
//	private static String createTabs(int tabCount) {
//		String s = "";
//
//		for (int i = 0; i < tabCount; i++)
//			s += "\t";
//
//		return s;
//	}
//
//	/**
//	 * Creates the json schema from elementary.
//	 * 
//	 * @param type
//	 *            the type
//	 * @param tabs
//	 *            the tabs
//	 * @return the string
//	 */
//	private static String createJsonSchemaFromElementary(String type, int tabs) {
//		/*
//		 * example: { "type" : "string" }
//		 */
//		String result;
//		result = "{" + "\n";
//		result += createTabs(tabs) + "\"type\": \"" + type + "\"" + "\n";
//		result += createTabs(tabs - 1) + "}";
//		return result;
//	}
//
//	/**
//	 * Creates the json schema from list.
//	 * 
//	 * @param listObjectType
//	 *            the list schema
//	 * @param tabs
//	 *            the tabs
//	 * @return the string
//	 */
//	private static String createJsonSchemaFromList(ListObjectType listObjectType,
//			int tabs) {
//		/*
//		 * example: { "type": "array", "items": { ... } }
//		 */
//		String result = "{" + "\n";
//		result += createTabs(tabs) + "\"type\": \"array\"," + "\n";
//		result += createTabs(tabs) + "\"items\": ";
//
//		List<GenericValueType> list = listObjectType.getAttributes();
//		for (int i = 0; i < list.size(); i++) {
//			GenericValueType genericValueType = list.get(i);
//
//			if (i != list.size() - 1) {
//				result += createJsonSchema(genericValueType, tabs) + "," + "\n";
//			} else {
//				result += createJsonSchema(genericValueType, tabs) + "\n";
//			}
//		}
//
//		result += createTabs(tabs - 1) + "}";
//
//		return result;
//	}
//
//	/**
//	 * Creates the json schema from map.
//	 * 
//	 * @param mapObjectType
//	 *            the map schema
//	 * @param tabs
//	 *            the tabs
//	 * @return the string
//	 */
//	@SuppressWarnings("unchecked")
//	private static String createJsonSchemaFromMap(MapComplexValueType mapObjectType, int tabs) {
//		/*
//		 * { "type": "object", "properties": { ... } }
//		 */
//
//		String result = "{" + "\n";
//		result += createTabs(tabs) + "\"type\": \"object\"," + "\n";
//		result += createTabs(tabs) + "\"properties\": {" + "\n";
//
//		Map<String, GenericValueType> map = mapObjectType.getMap();
//		Object[] arr = map.entrySet().toArray();
//		for (int i = 0; i < arr.length; i++) {
//			Entry<String, GenericValueType> entry = (Entry<String, GenericValueType>) arr[i];
//			if (i != arr.length - 1) {
//				result += createTabs(tabs + 1) + "\"" + entry.getKey() + "\": "
//						+ createJsonSchema(entry.getValue(), tabs + 1) + ","
//						+ "\n";
//			} else {
//				result += createTabs(tabs + 1) + "\"" + entry.getKey() + "\": "
//						+ createJsonSchema(entry.getValue(), tabs + 1) + "\n";
//			}
//		}
//
//		result += createTabs(tabs) + "}" + "\n";
//		result += createTabs(tabs - 1) + "}";
//
//		return result;
//	}
//
	/**
	 * Validate generic valus fits schema.
	 * 
	 * @param gv
	 *            the gv
	 * @param objectType
	 *            the db schema
	 * @return true, if successful
	 */
	public static boolean validateGenericValusFitsSchema(GenericEntity gv,
			ObjectType objectType) {
//		ObjectMapper mapper = new ObjectMapper();
//
//		String json;
//		try {
//			json = mapper.writeValueAsString(gv);
//		} catch (JsonProcessingException e) {
//			String error = "Could not convert GenericValue to json"
//					+ e.getMessage();
//			Logging.error(SchemaManager.class, error);
//			System.err.println(error);
//			return false;
//		}
//
//		try {
//			JsonNode jsonNode = mapper.readTree(json);
//			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
//			String jsonSchema = createJsonSchema(objectType);
//
//			// validate jsonSchema
//			JsonNode jn = JsonLoader.fromString(jsonSchema);
//			SyntaxValidator validator = factory.getSyntaxValidator();
//			boolean result = validator.schemaIsValid(jn);
//			if (result == false) {
//				String error = "schema is not valid";
//				Logging.error(SchemaManager.class, error);
//				System.err.println(error);
//				return false;
//			}
//
//			// validate
//			JsonValidator jsonValidator = factory.getValidator();
//			ProcessingReport report = jsonValidator.validate(jn, jsonNode);
//			result = report.isSuccess();
//			if (result == false) {
//				String error = "Could not validate json: " + report;
//				Logging.error(SchemaManager.class, error);
//				System.err.println(error);
//				return false;
//			}
//
//		} catch (IOException e) {
//			String error = "Could not validate json" + e.getMessage();
//			Logging.error(SchemaManager.class, error);
//			System.err.println(error);
//			return false;
//		} catch (ProcessingException e) {
//			String error = "Could not validate json" + e.getMessage();
//			Logging.error(SchemaManager.class, error);
//			System.err.println(error);
//			return false;
//		}
		return true;
	}

	//TODO
	
}
