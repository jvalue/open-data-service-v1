package org.jvalue.ods.rest.v2.jsonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiableWithMetaData;
import org.jvalue.ods.api.jsonapi.MetaData;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public class JsonApiResource extends JsonApiData{

	private final JsonApiIdentifiable entity;
	private final MetaData meta;

	private JsonApiResource(JsonApiIdentifiable entity, URI uri, MetaData meta) {
		super(uri, entity);
		this.entity = entity;
		this.meta = meta;
	}

	public JsonApiResource(JsonApiIdentifiable entity,
						   URI uri) {
		this(entity, uri, null);
	}

	public JsonApiResource(JsonApiIdentifiableWithMetaData entity, URI uri) {
		this(entity, uri, entity.getMetaData());
	}

	public JsonApiResourceIdentifier toIdentifier() {
	    return new JsonApiResourceIdentifier(entity, uri);
    }


	public JsonApiData setSelfLink(){
		setLink("self", uri);
		return this;
	}


	public void setLink(String name,
						URI ref) {
		if(links == null) {
			links = new HashMap<>();
		}
		links.put(name, ref);
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	public MetaData getMeta() {
		return meta;
	}


	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Map<String, URI> getLinks() {
		return links;
	}


	public String getId() {
		return id;
	}


	public String getType() {
		return type;
	}


	@JsonProperty("attributes")
	@JsonIgnoreProperties(value = {"id", "metaData"})
	public JsonApiIdentifiable getEntity() {
		return entity;
	}
}
