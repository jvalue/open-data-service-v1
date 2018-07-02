package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jvalue.commons.utils.Assert;

import java.util.UUID;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode createJsonNode(Object object) {
        return objectMapper.valueToTree(object);
    }


    public static String getIdFromObject(Object from) {

        String id;

        try {
            id = getPropertyValueString(from, "id");
        } catch(IllegalArgumentException e) {
            //todo: log warning
            // id has to be persisted
            id = UUID.randomUUID().toString();
        }
        return id;
    }


    public static String getPropertyValueString(Object from,
                                                String propertyName) {

        Assert.assertNotNull(from);

        JsonNode val = doGetPropertyValue(from, propertyName);
        if (!val.isTextual()) {
            throw new IllegalArgumentException("Property " + propertyName + " is expected to be a String but is " + val.getNodeType().name());
        }
        return val.textValue();
    }


    /**
     *
     * @param from the java object
     * @param propertyName the name of the property whose value shall be returned
     * @return the value of the property as JsonNode or null, if no such property
     * exists
     */
    public static JsonNode getPropertyValueNode(Object from,
                                                String propertyName) {

        Assert.assertNotNull(from);

        try {
            return doGetPropertyValue(from, propertyName);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    
    private static JsonNode doGetPropertyValue(Object from,
                                               String propertyName) {

        JsonNode objNode = objectMapper.valueToTree(from);

        if(!objNode.has(propertyName)) {
            throw new IllegalArgumentException("Attribute " + propertyName + " does not exist");
        }
        return objNode.get(propertyName);
    }

}
