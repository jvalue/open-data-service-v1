package org.jvalue.ods.decoupleDatabase;

import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.DocumentOperationResult;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.views.DataView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDataRepository<V> extends IRepository<V> {
	JsonNode findByDomainId(String domainId);

	List<JsonNode> executeQuery(DataView view, String param);

	void addView(DataView dataView);

	void removeView(DataView view);

	boolean containsView(DataView dataView);

	Map<String, JsonNode> executeBulkGet(Collection<String> ids);

	Collection<DocumentOperationResult> executeBulkCreateAndUpdate(Collection<JsonNode> data);

	Data executePaginatedGet(String startDomainId, int count);

	void removeAll();

	void compact();
}
