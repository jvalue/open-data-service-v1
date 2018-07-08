package org.jvalue.ods.rest.v2.jsonApi;

import java.util.HashMap;
import java.util.Map;

public class TestEntityProvider {

    public final static String TEST_ID = "testId";
    public static final String TEST_COLLECTION = "testCollection";
    public static final String COLLECTION_PATH = "scheme://authority/path/to/"+TEST_COLLECTION;
    public final static String ENTITY_PATH = COLLECTION_PATH+"/entity";

    public static class MinimalEntity implements TestEntity {
        private final String id = TEST_ID;

        public String getId() {
            return id;
        }
    }

    public static class CollectableEntity implements TestEntity {
        private final String id;

        public CollectableEntity(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class EntityWithAttributes implements TestEntity {
        private final String id = TEST_ID;
        private final int intAttribute = 1;
        private final String stringAttribute = "";
        private final Map<String, String> mapAttribute = new HashMap<>();
        private final NestedClass nestedAttribute = new NestedClass();

        public EntityWithAttributes() {
            mapAttribute.put("1","1");
        }

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

        public static class NestedClass{
            private final String nestedAttribute = "nested";

            public String getNestedAttribute() {
                return nestedAttribute;
            }
        }

    }

    public interface TestEntity {    };
}

