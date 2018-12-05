package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.JsonValue;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.jvalue.ods.utils.HttpUtils.appendTrailingSlash;

/**
 * Class representation of resource collections.
 * @JsonValue annotation on getResources() method marks resource array as serialization root.
 */
public class JsonApiResourceCollection implements JsonApiData {

	private final List<JsonApiResource> resources = new LinkedList<>();

	public JsonApiResourceCollection(Collection<? extends JsonApiIdentifiable> entities, URI collectionUri) {
		entities.forEach(
			e -> resources.add(
				new JsonApiResource(e, collectionUri.resolve(e.getId()))));
		this.resources.forEach(JsonApiResource::addSelfLink);

	}

	@JsonValue
	public List<JsonApiResource> getResources() {
		return resources;
	}

	@Override
	public JsonApiData restrictTo(String attribute) {
		resources.forEach(
			r -> r.restrictTo(attribute)
		);

		return this;
	}

	@Override
	public JsonApiData addRelationship(String name, Collection<? extends JsonApiIdentifiable> relatedCollection, URI location) {
		resources.forEach(
			r -> r.addRelationship(name, relatedCollection, location)
		);

		return this;
	}

	@Override
	public JsonApiData addRelationship(String name, JsonApiIdentifiable related, URI location) {
		resources.forEach(
			r -> r.addRelationship(name, related, location)
		);

		return this;
	}

	@Override
	public URI getRelationshipUri(JsonApiIdentifiable related) {

		return resources
			.stream()
			.filter(r -> r.hasRelationshipTo(related))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("relationship " + related.getId() + " does not exist."))
			.getRelationshipUri(related);
	}

	@Override
	public boolean hasRelationshipTo(JsonApiIdentifiable related) {
		return resources
				.stream()
				.anyMatch(r -> r.hasRelationshipTo(related));
	}

	@Override
	public void setResourceCollectionURI(URI collectionURI) {
		resources.forEach(r -> {
				r.getLinks().clear();
				r.addLink(JsonLinks.SELF, appendTrailingSlash(collectionURI).resolve(r.getId()));
			});
	}
}
