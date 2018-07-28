package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;
import org.jvalue.commons.EntityBase;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MongoDbQuery extends Document implements EntityBase {

	@NotNull
	private final String id;


	/**
	 * Create a new map reduce view on the data.
	 *
	 * @param id the id of the view
	 */
	@JsonCreator
	public MongoDbQuery(
		@JsonProperty("id") String id) {
		this.id = id;
	}


	@Override
	public String getId() {
		return id;
	}
}
