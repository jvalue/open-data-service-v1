package org.jvalue.ods.rest.v2.JsonApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.SerializationException;

import java.net.URI;
import java.util.*;

import static org.jvalue.ods.utils.JsonUtils.getPropertyValueString;

public class JsonApiDocument<T> {
    private Map<String, String> links = new HashMap<>();
    private Optional<JsonNode> meta = Optional.empty();
    private Optional<JsonApiResource<T>> dataObject;
    private Optional<List<JsonApiResource<T>>> dataArray;
    private Optional<JsonNode> errors = Optional.empty();
    private Optional included = Optional.empty();
    private final String uri;

    public JsonApiDocument(T entity, URI uri) {
        this.uri = uri.toString();
        this.dataObject = Optional.of(new JsonApiResource<>(entity, uri.toString()));
        this.dataArray = Optional.empty();
        initLinks();
    }

    public JsonApiDocument(T entity, URI uri, String id) {
        this.uri = uri.toString();
        this.dataObject = Optional.of(new JsonApiResource<>(entity, uri.toString(), id));
        this.dataArray = Optional.empty();
        initLinks();
    }

    public JsonApiDocument(Collection<T> entityCollection, URI uri) {
        this.uri = uri.toString();
        ArrayList<JsonApiResource<T>> dataList = new ArrayList<>();
        entityCollection.forEach(el -> addAsJsonApiResource(el, dataList));
        this.dataArray = Optional.of(dataList);
        this.dataObject = Optional.empty();
        initLinks();
    }

    public Map<String, String> getLinks() {
        return links;
    }

    @JsonProperty("data")
    public Object getData() {
        if(dataArray.isPresent()) {
            return dataArray.get();
        }
        else if (dataObject.isPresent()){
            return dataObject.get();
        }
        else throw new IllegalArgumentException("no data available");
    }

    private void addAsJsonApiResource(T el, Collection<JsonApiResource<T>> coll) {
        try {
            coll.add(new JsonApiResource<>(el, uri +"/"+getPropertyValueString(el, "id")));
        }
        catch (IllegalArgumentException e) {
            throw new SerializationException("JsonApiSerialization of " + el.getClass().getCanonicalName() + " failed", e);
        }
    }

    private void initLinks() {
        links.put("self", uri);
    }

//    private void assertValidState() {
//        if(data.isPresent() && dataArray.isPresent()
//                || errors.isPresent() && (data.isPresent() || dataArray.isPresent())) {
//            throw new IllegalStateException("Only one of data, dataArray or errors is allowed to be present");
//        }
//    }
}
