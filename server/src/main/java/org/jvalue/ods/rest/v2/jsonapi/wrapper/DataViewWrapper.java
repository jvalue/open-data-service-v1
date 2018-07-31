package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.ods.api.views.DataView;

import java.util.Collection;
import java.util.stream.Collectors;

public class DataViewWrapper extends DataView implements JsonApiIdentifiable {


	private DataViewWrapper(String id, String mapFunction, String reduceFunction) {
		super(id, mapFunction, reduceFunction);
	}


	@Override
	public String getType() {
		return DataView.class.getSimpleName();
	}


	public static DataViewWrapper from(DataView dataView) {
		return new DataViewWrapper(dataView.getId(), dataView.getMapFunction(), dataView.getReduceFunction());
	}


	public static Collection<DataViewWrapper> fromCollection(Collection<DataView> dataViews) {
		return dataViews.stream()
			.map(DataViewWrapper::from)
			.collect(Collectors.toList());
	}
}
