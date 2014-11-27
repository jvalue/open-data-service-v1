package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.UpdateConflictException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.ektorp.support.View;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;

import java.util.List;

@View( name = "all", map = "function(doc) { emit(null, doc) }")
public final class SourceDataRepository extends CouchDbRepositorySupport<JsonNode> {

	private static final String DESIGN_DOCUMENT_ID = "_design/" + JsonNode.class.getSimpleName();
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	private final CouchDbConnector connector;
	private final DbView domainIdView;

	@Inject
	SourceDataRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(JsonNode.class, couchDbInstance.createConnector(databaseName, true));
		this.connector = couchDbInstance.createConnector(databaseName, true);
		initStandardDesignDocument();

		String domainIdViewName = "findByDomainId";
		StringBuilder mapBuilder = new StringBuilder();
		mapBuilder.append("function(doc) { if (");
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
		mapBuilder.append(keyBuilder.toString());
		mapBuilder.append(") emit(");
		mapBuilder.append(keyBuilder.toString());
		mapBuilder.append(", doc) }");
		this.domainIdView = new DbView(domainIdViewName, mapBuilder.toString());
		if (!containsView(domainIdView)) addView(domainIdView);
	}


	public JsonNode findByDomainId(String domainId) {
		List<JsonNode> resultList = executeQuery(domainIdView, domainId);
		if (resultList.isEmpty()) return null;
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

}
