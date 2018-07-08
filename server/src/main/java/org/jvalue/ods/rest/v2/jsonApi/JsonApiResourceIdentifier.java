package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResourceIdentifier<T> extends JsonApiData<T>{

    private final String id;
    private final String type;
    private Map<String, URI> links;

    public JsonApiResourceIdentifier(T entity, URI uri) {
        super(uri);
        this.id = getIdFromObject(entity);
        this.type = getUriRootElement(uri.toString());
    }


    public JsonApiResourceIdentifier<T> initLinks() {
        this.links = new HashMap<>();
        links.put("self",uri);
        return this;
    }


    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, URI> getLinks() {
        return links;
    }
}
