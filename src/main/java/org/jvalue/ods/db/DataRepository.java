package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.UpdateConflictException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;

import java.util.List;

public final class DataRepository extends CouchDbRepositorySupport<JsonNode> {

	private static final String DESIGN_DOCUMENT_ID = "_design/" + JsonNode.class.getSimpleName();
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	private final CouchDbConnector connector;
	private final DbView domainIdView;

	@Inject
	DataRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(JsonNode.class, couchDbInstance.createConnector(databaseName, true));
		this.connector = couchDbInstance.createConnector(databaseName, true);
		initStandardDesignDocument();

		domainIdView = createDomainIdView(domainIdKey);
		if (!containsView(domainIdView)) addView(domainIdView);

		DbView allView = createAllView(domainIdKey);
		if (!containsView(allView)) addView(allView);
	}


	public JsonNode findByDomainId(String domainId) {
		List<JsonNode> resultList = executeQuery(domainIdView, domainId);
		if (resultList.isEmpty()) throw new DocumentNotFoundException(domainId);
		else if (resultList.size() == 1) return resultList.get(0);
		else throw new IllegalStateException("found more than one element for given domain id");
	}


	public List<JsonNode> executeQuery(DbView view, String param) {
		return queryView(view.getViewName(), param);
	}


	public List<JsonNode> executeQuery(DbView view) {
		return queryView(view.getViewName());
	}


	public void addView(DbView dbView) {
		Assert.assertNotNull(dbView);

		DesignDocument designDocument;
		boolean update = false;

		if (connector.contains(DESIGN_DOCUMENT_ID)) {
			designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
			update = true;
		} else {
			designDocument = designFactory.newDesignDocumentInstance();
			designDocument.setId(DESIGN_DOCUMENT_ID);
		}

		DesignDocument.View view = new DesignDocument.View(dbView.getMapFunction());
		designDocument.addView(dbView.getViewName(), view);

		try {
			if (update) connector.update(designDocument);
			else connector.create(designDocument);
		} catch (UpdateConflictException uce) {
			// TODO throw some meaningful exception
			Log.error("failed to add view", uce);
		}
	}


	public boolean containsView(DbView dbView) {
		Assert.assertNotNull(dbView);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return false;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		return designDocument.containsView(dbView.getViewName());
	}


	private DbView createDomainIdView(JsonPointer domainIdKey) {
		String domainIdViewName = "findByDomainId";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(", doc) }");
		return new DbView(domainIdViewName, mapBuilder.toString());
	}


	private DbView createAllView(JsonPointer domainIdKey) {
		String domainIdViewName = "all";
		String domainIdProperty = createDomainIdJavascriptProperty(domainIdKey);

		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
		mapBuilder.append(domainIdProperty);
		mapBuilder.append(") emit(null,doc) }");
		return new DbView(domainIdViewName, mapBuilder.toString());
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
