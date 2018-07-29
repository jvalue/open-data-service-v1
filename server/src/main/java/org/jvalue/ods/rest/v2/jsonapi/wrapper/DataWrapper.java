package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.data.Cursor;
import org.jvalue.ods.api.data.Data;

import java.util.List;

public class DataWrapper extends Data implements JsonApiIdentifiable{

	private DataWrapper(List<JsonNode> result, Cursor cursor) {
		super(result, cursor);
	}


	@Override
	public String getId() {
		return getCursor().getNext() + "+" + getCursor().getCount();
	}


	@Override
	public String getType() {
		return Data.class.getSimpleName();
	}


	/**
	 * Wrap instance of Data class
	 * @param data the data to be wrapped
	 * @return a DataWrapper which wraps {@param data}
	 */
	public static DataWrapper from(Data data) {
		return new DataWrapper(
			data.getResult(),
			data.getCursor());
	}
}
