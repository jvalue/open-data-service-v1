package org.jvalue.ods.rest.v2.jsonapi;

import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jvalue.ods.rest.v2.jsonapi.TestEntityProvider.TEST_ID;

public class TestEntityProvider {

	public final static String TEST_ID = "testId";
	public static final String TEST_COLLECTION = "testCollection";
	public static final String COLLECTION_PATH = "scheme://authority/path/to/" + TEST_COLLECTION;
	public final static String ENTITY_PATH = COLLECTION_PATH + "/entity";

	public static JsonApiIdentifiable createMinimalEntity() {
		return () -> TEST_ID;
	}

	public static JsonApiIdentifiable createCollectableEntity(int id) {
		return () -> String.valueOf(id);
	}

	public static JsonApiIdentifiable createEntityWithAttributes() {
		return new JsonApiIdentifiable() {
			private final String id = TEST_ID;
			private final int intAttribute = 1;
			private final String stringAttribute = "";
			private final Map<String, String> mapAttribute = new HashMap<String, String>() {{
				put("1","1");
			}};

			private final NestedClass nestedAttribute = new NestedClass();

			public String getId() {
				return id;
			}

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
}

