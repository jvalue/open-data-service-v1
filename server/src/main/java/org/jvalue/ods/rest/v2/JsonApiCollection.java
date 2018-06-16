package org.jvalue.ods.rest.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

import static org.jvalue.ods.utils.JsonUtils.getPropertyValueString;

public class JsonApiCollection<T> extends JsonApiObject<T>{

    @JsonProperty("data")
    public final ArrayList<JsonApiObject<T>> contents;



    public JsonApiCollection(String selfReference) {
        super(selfReference);
        contents = new ArrayList<>();
    }

    public JsonApiCollection(Collection<T> collection, String selfReference) {
        this(selfReference);
//        collection.forEach(el -> contents.add(
//                new JsonApiIndividual<>(el, selfReference+getPropertyValueString(el, "id"))));
        collection.forEach(el -> addAsJsonApiObject(el, contents));
    }

    private void addAsJsonApiObject(T el, Collection<JsonApiObject<T>> coll) {
        try {
            coll.add(new JsonApiIndividual<>(el, selfReference+"/"+getPropertyValueString(el, "id")));
        }
        catch (IllegalArgumentException e) {
            coll.add(new JsonApiIndividual<>(el, selfReference+"/noId"));

        }
    }

}
