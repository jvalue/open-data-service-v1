package org.jvalue.ods.rest.v2.jsonApi;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.jvalue.ods.utils.JsonUtils.getIdFromObject;
import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResourceIdentifier<T> extends JsonApiData<T>{

    private final String id;
    private final String type;
    private final Map<String, URI> links;

    public JsonApiResourceIdentifier(T entity, URI uri) {
        super(uri);
        this.id = getIdFromObject(entity);
        this.type = getUriRootElement(uri.toString());
        this.links = new HashMap<>();
        initLinks();
    }


    private void initLinks() {
        links.put("self",uri);
    }


    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }


    public Map<String, URI> getLinks() {
        return links;
    }
}
