package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.views.DataView;

import java.util.Collection;
import java.util.stream.Collectors;

@Schema(name = "dataViewData")
public class DataViewWrapper implements JsonApiIdentifiable {

	private final DataView dataView;

	private DataViewWrapper(String id, String mapFunction, String reduceFunction) {
		this.dataView = new DataView(id, mapFunction, reduceFunction);
	}


	@Schema(name = "attributes", required = true)
	@JsonUnwrapped
	@JsonIgnoreProperties({"id", "type"})
	public DataView getDataView() {
		return dataView;
	}


	@Override
	@Schema(example = "myView", required = true)
	public String getId() {
		return dataView.getId();
	}


	@Override
	@Schema(allowableValues = "DataView", required = true)
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
