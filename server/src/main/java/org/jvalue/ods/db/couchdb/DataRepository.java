package org.jvalue.ods.db.couchdb;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.*;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.api.data.Cursor;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.GenericDataRepository;

import java.util.*;


public final class DataRepository extends CouchDbRepositorySupport<JsonNode> implements GenericDataRepository<CouchDbDataView, JsonNode> {

	private static final String DESIGN_DOCUMENT_NAME = "Data";
	private static final String DESIGN_DOCUMENT_ID = "_design/" + DESIGN_DOCUMENT_NAME;
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	private final CouchDbConnector connector;
	private final CouchDbDataView domainIdView;
	private final CouchDbDataView revAndIdByDomainIdView;


	@Inject
	public DataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(JsonNode.class, (CouchDbConnector) dbConnectorFactory.createConnector(databaseName, true), DESIGN_DOCUMENT_NAME);
		this.connector = (CouchDbConnector) dbConnectorFactory.createConnector(databaseName, true);
		initStandardDesignDocument();

		domainIdView = createObjectByDomainIdView(domainIdKey);
		if (!containsQuery(domainIdView)) addQuery(domainIdView);

		revAndIdByDomainIdView = createIdAndRevByDomainIdView(domainIdKey);
		if (!containsQuery(revAndIdByDomainIdView)) addQuery(revAndIdByDomainIdView);

		CouchDbDataView allView = createAllView(domainIdKey);
		if (!containsQuery(allView)) addQuery(allView);
	}


	@Override
	public JsonNode findById(String Id) {
		return findByDomainId(Id);
	}


	@Override
	public JsonNode findByDomainId(String domainId) {
		List<JsonNode> resultList = executeQuery(domainIdView, domainId);
		if (resultList.isEmpty()) throw new DocumentNotFoundException(domainId);
		else if (resultList.size() == 1) return resultList.get(0);
		else throw new IllegalStateException("found more than one element for given domain id");
	}


	public List<JsonNode> executeQuery(CouchDbDataView view, String param) {
		ViewQuery query = createQuery(view.getId());

		if (param != null) query = query.key(param);

		return connector.queryView(query, JsonNode.class);
	}


	public void addQuery(CouchDbDataView dataView) {
		Assert.assertNotNull(dataView);

		DesignDocument designDocument;
		boolean update = false;

		if (connector.contains(DESIGN_DOCUMENT_ID)) {
			designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
			update = true;
		} else {
			designDocument = designFactory.newDesignDocumentInstance();
			designDocument.setId(DESIGN_DOCUMENT_ID);
		}

		DesignDocument.View view;
		if (dataView.getReduceFunction() == null) view = new DesignDocument.View(dataView.getMapFunction());
		else view = new DesignDocument.View(dataView.getMapFunction(), dataView.getReduceFunction());
		designDocument.addView(dataView.getId(), view);

		if (update) connector.update(designDocument);
		else connector.create(designDocument);
	}


	public void removeQuery(CouchDbDataView view) {
		Assert.assertNotNull(view);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		designDocument.removeView(view.getId());
		connector.update(designDocument);
	}


	public boolean containsQuery(CouchDbDataView queryObject) {
		Assert.assertNotNull(queryObject);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return false;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		return designDocument.containsView(queryObject.getId());
	}


	@Override
	public Map<String, JsonNode> getData(Collection<String> ids) {
		return executeBulkGet(ids);
	}


	private Map<String, JsonNode> executeBulkGet(Collection<String> ids) {
		ViewQuery query = new ViewQuery()
			.designDocId(DESIGN_DOCUMENT_ID)
			.viewName(domainIdView.getId())
			.includeDocs(true)
			.keys(ids);

		Map<String, JsonNode> nodes = new HashMap<>();
		for (ViewResult.Row row : connector.queryView(query).getRows()) {
			nodes.put(row.getKey(), row.getDocAsNode());
		}
		return nodes;
	}


	@Override
	public Collection<GenericDocumentOperationResult> writeData(Collection<JsonNode> data) {
		Collection<DocumentOperationResult> documentOperationResults = executeBulkCreateAndUpdate(data);
		List<GenericDocumentOperationResult> documentResults = new LinkedList<>();

		for (DocumentOperationResult res : documentOperationResults) {
			documentResults.add(GenericDocumentOperationResult.newInstance(res.getId(), res.getError(), res.getReason()));
		}

		return documentResults;
	}


	private Collection<DocumentOperationResult> executeBulkCreateAndUpdate(Collection<JsonNode> data) {
		return connector.executeBulk(data);
	}


	@Override
	public Data getPaginatedData(String startDomainId, int count) {
		return executePaginatedGet(startDomainId, count);
	}


	/**
	 * Cursor based pagination of the data in this repository based on ascending sorted domain ids.
	 *
	 * @param startDomainId the start id of the requested page or null if the result should start
	 *                      at the first entry.
	 * @param count         how many entries should be in the page. Must be greater than 0.
	 * @return cursor based pagination of the data
	 */
	private Data executePaginatedGet(String startDomainId, int count) {
		ViewQuery query = new ViewQuery()
			.designDocId(DESIGN_DOCUMENT_ID)
			.viewName(domainIdView.getId())
			.includeDocs(true)
			.descending(false)
			.limit(count + 1);
		if (startDomainId != null) query = query.startKey(startDomainId);

		List<JsonNode> result = new LinkedList<>();
		int resultCount = 0;
		String nextStartDomainId = null;
		boolean hasNext = false;

		for (ViewResult.Row row : connector.queryView(query).getRows()) {
			if (resultCount == count) {
				hasNext = true;
				nextStartDomainId = row.getKey();
				break;
			}

			result.add(row.getDocAsNode());
			++resultCount;
		}

		Cursor cursor = new Cursor(nextStartDomainId, hasNext, resultCount);
		return new Data(result, cursor);
	}


	public void removeAll() {
		ViewQuery query = new ViewQuery()
			.designDocId(DESIGN_DOCUMENT_ID)
			.viewName(revAndIdByDomainIdView.getId());

		Collection<JsonNode> deletedObjects = new LinkedList<>();
		for (ViewResult.Row row : connector.queryView(query).getRows()) {
			ObjectNode node = (ObjectNode) row.getValueAsNode();
			node.put("_deleted", true);
			deletedObjects.add(node);
		}

		executeBulkCreateAndUpdate(deletedObjects);
	}


	/**
	 * Compacts the underlying CouchDb database by removing old revisions of documents.
	 */
	public void compact() {
		connector.compact();
	}


	private CouchDbDataView createObjectByDomainIdView(JsonPointer domainIdKey) {
		String viewName = "findObjectByDomainId";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(", doc) }");
		return new CouchDbDataView(viewName, mapBuilder.toString());
	}


	private CouchDbDataView createIdAndRevByDomainIdView(JsonPointer domainIdKey) {
		String viewName = "findIdAndRevByDomainId";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(", { _id : doc._id, _rev : doc._rev }) }");
		return new CouchDbDataView(viewName, mapBuilder.toString());
	}


	private CouchDbDataView createAllView(JsonPointer domainIdKey) {
		String viewName = "all";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(" != null) emit(null,doc) }");
		return new CouchDbDataView(viewName, mapBuilder.toString());
	}


	private String createDomainIdJavascriptProperty(JsonPointer domainIdKey) {
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append("doc");
		JsonPointer pointer = domainIdKey;
		while (pointer != null && !pointer.toString().isEmpty()) {
			if (pointer.mayMatchProperty()) {
				keyBuilder.append(".");
				keyBuilder.append(pointer.getMatchingProperty());
			} else {
				keyBuilder.append("[");
				keyBuilder.append(pointer.getMatchingIndex());
				keyBuilder.append("]");
			}
			pointer = pointer.tail();
		}
		return keyBuilder.toString();
	}
}
