package org.jvalue.ods.rest.v2.jsonapi.response;

import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.util.HashMap;
import java.util.Map;

public class TestEntityProvider {

	public final static String TEST_ID = "testId";
	public static final String TEST_COLLECTION = "testCollection";
	public static final String COLLECTION_PATH = "scheme://authority/path/to/" + TEST_COLLECTION;
	public final static String ENTITY_PATH = COLLECTION_PATH + "/entity";

	public static JsonApiIdentifiable createMinimalEntity() {
		return new JsonApiResourceIdentifier(TEST_ID, TestEntityProvider.class.getSimpleName());
	}

	public static JsonApiIdentifiable createCustomMinimalEntity(String id) {
		return new JsonApiResourceIdentifier(id, TestEntityProvider.class.getSimpleName());
	}

	public static JsonApiIdentifiable createEntityWithAttributes(String identifier) {
		return new JsonApiIdentifiable() {
			private String id = identifier;
			private final String type = "TestJsonApiIdentifiable";
			private final int intAttribute = 1;
			private final String stringAttribute = "";
			private final Map<String, String> mapAttribute = new HashMap<String, String>() {{
				put("1","1");
			}};

			private final NestedClass nestedAttribute = new NestedClass();

			public String getId() {
				return id;
			}

			public String getType() {return type;}

			public int getIntAttribute() {
				return intAttribute;
			}

			public String getStringAttribute() {
				return stringAttribute;
			}

			public Map<String, String> getMapAttribute() {
				return mapAttribute;
			}

			public NestedClass getNestedAttribute() {
				return nestedAttribute;
			}

			class NestedClass {
				private final String nestedAttribute = "nested";

				public String getNestedAttribute() {
					return nestedAttribute;
				}
			}
		};
	}

	public static JsonApiIdentifiable createEntityWithAttributes() {
		return createEntityWithAttributes(TEST_ID);
	}
}

