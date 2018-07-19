package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.DocumentOperationResult;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.decoupleDatabase.IDataRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CouchDbDataRepositoryWrapper implements IDataRepository<JsonNode> {

	private final DataRepository dataRepository;

	@Inject
	public CouchDbDataRepositoryWrapper(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey){
		this.dataRepository = new DataRepository(dbConnectorFactory, databaseName, domainIdKey);
	}


	@Override
	public JsonNode findByDomainId(String domainId) {
		return dataRepository.findByDomainId(domainId);
	}


	@Override
	public List<JsonNode> executeQuery(DataView view, String param) {
		return dataRepository.executeQuery(view, param);
	}


	@Override
	public void addView(DataView dataView) {
		dataRepository.addView(dataView);
	}


	@Override
	public void removeView(DataView view) {
		dataRepository.removeView(view);
	}


	@Override
	public boolean containsView(DataView dataView) {
		return dataRepository.containsView(dataView);
	}


	@Override
	public Map<String, JsonNode> executeBulkGet(Collection<String> ids) {
		return dataRepository.executeBulkGet(ids);
	}


	@Override
	public Collection<DocumentOperationResult> executeBulkCreateAndUpdate(Collection<JsonNode> data) {
		return dataRepository.executeBulkCreateAndUpdate(data);
	}


	@Override
	public Data executePaginatedGet(String startDomainId, int count) {
		return dataRepository.executePaginatedGet(startDomainId, count);
	}


	@Override
	public void removeAll() {
		dataRepository.removeAll();
	}


	@Override
	public void compact() {
		dataRepository.compact();
	}


	@Override
	public JsonNode findById(String Id) {
		return dataRepository.findByDomainId(Id);
	}


	@Override
	public void add(JsonNode value) {
		dataRepository.add(value);
	}


	@Override
	public void update(JsonNode value) {
		dataRepository.update(value);
	}


	@Override
	public void remove(JsonNode value) {
		dataRepository.remove(value);
	}


	@Override
	public List<JsonNode> getAll() {
		return dataRepository.getAll();
	}
}
