package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.DocumentOperationResult;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.utils.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DataRepository extends CouchDbRepositorySupport<JsonNode> {

	private static final String DESIGN_DOCUMENT_ID = "_design/" + JsonNode.class.getSimpleName();
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	private final CouchDbConnector connector;
	private final DataView domainIdView;
	private final DataView revAndIdBydomainIdView;


	@Inject
	DataRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(JsonNode.class, couchDbInstance.createConnector(databaseName, true));
		this.connector = couchDbInstance.createConnector(databaseName, true);
		initStandardDesignDocument();

		domainIdView = createObjectByDomainIdView(domainIdKey);
		if (!containsView(domainIdView)) addView(domainIdView);

		revAndIdBydomainIdView = createIdAndRevByDomainIdView(domainIdKey);
		if (!containsView(revAndIdBydomainIdView)) addView(revAndIdBydomainIdView);

		DataView allView = createAllView(domainIdKey);
		if (!containsView(allView)) addView(allView);
	}


	public JsonNode findByDomainId(String domainId) {
		List<JsonNode> resultList = executeQuery(domainIdView, domainId);
		if (resultList.isEmpty()) throw new DocumentNotFoundException(domainId);
		else if (resultList.size() == 1) return resultList.get(0);
		else throw new IllegalStateException("found more than one element for given domain id");
	}


	public List<JsonNode> executeQuery(DataView view, String param) {
		if (param == null) return queryView(view.getId());
		else return queryView(view.getId(), param);
	}


	public void addView(DataView dataView) {
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


	public void removeView(DataView view) {
		Assert.assertNotNull(view);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		designDocument.removeView(view.getId());
		connector.update(designDocument);
	}


	public boolean containsView(DataView dataView) {
		Assert.assertNotNull(dataView);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return false;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		return designDocument.containsView(dataView.getId());
	}


	public Map<String, JsonNode> executeBulkGet(Collection<String> ids) {
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


	public Collection<DocumentOperationResult> executeBulkCreateAndUpdate(Collection<JsonNode> data) {
		return connector.executeBulk(data);
	}


	public void removeAll() {
		ViewQuery query = new ViewQuery()
				.designDocId(DESIGN_DOCUMENT_ID)
				.viewName(revAndIdBydomainIdView.getId());

		Collection<JsonNode> deletedObjects = new LinkedList<>();
		for (ViewResult.Row row : connector.queryView(query).getRows()) {
			ObjectNode node = (ObjectNode) row.getValueAsNode();
			node.put("_deleted", true);
			deletedObjects.add(node);
		}

		executeBulkCreateAndUpdate(deletedObjects);
	}


	private DataView createObjectByDomainIdView(JsonPointer domainIdKey) {
		String viewName = "findObjectByDomainId";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(", doc) }");
		return new DataView(viewName, mapBuilder.toString());
	}


	private DataView createIdAndRevByDomainIdView(JsonPointer domainIdKey) {
		String viewName = "findIdAndRevByDomainId";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(", { _id : doc._id, _rev : doc._rev }) }");
		return new DataView(viewName, mapBuilder.toString());
	}


	private DataView createAllView(JsonPointer domainIdKey) {
		String viewName = "all";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(null,doc) }");
		return new DataView(viewName, mapBuilder.toString());
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
