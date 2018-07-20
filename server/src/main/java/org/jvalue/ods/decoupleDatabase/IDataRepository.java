package org.jvalue.ods.decoupleDatabase;

import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.DocumentOperationResult;
import org.jvalue.commons.db.IRepository;
import org.jvalue.ods.api.data.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDataRepository<View, V> extends IRepository<V> {
	JsonNode findByDomainId(String domainId);

	List<JsonNode> executeQuery(View view, String param);

	void addView(View view);

	void removeView(View view);

	boolean containsView(View view);

	Map<String, JsonNode> executeBulkGet(Collection<String> ids);

	Collection<DocumentOperationResult> executeBulkCreateAndUpdate(Collection<JsonNode> data);

	Data executePaginatedGet(String startDomainId, int count);

	void removeAll();

	void compact();
}
