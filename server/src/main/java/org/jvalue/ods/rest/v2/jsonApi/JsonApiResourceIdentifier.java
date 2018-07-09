package org.jvalue.ods.rest.v2.jsonApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jvalue.ods.api.jsonApi.JsonApiIdentifiable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.jvalue.ods.utils.StringUtils.getUriRootElement;

public class JsonApiResourceIdentifier extends JsonApiData{

    private final String id;
    private final String type;
    private Map<String, URI> links;

    public JsonApiResourceIdentifier(JsonApiIdentifiable entity, URI uri) {
        super(uri);
        this.id = entity.getId();
        this.type = getUriRootElement(uri.toString());
    }


    public JsonApiResourceIdentifier initLinks() {
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
