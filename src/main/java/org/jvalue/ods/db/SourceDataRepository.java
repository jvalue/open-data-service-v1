package org.jvalue.ods.db;


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
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;

import java.util.List;

public final class SourceDataRepository extends CouchDbRepositorySupport<JsonNode> {

	private static final String DESIGN_DOCUMENT_ID = "_design/" + SourceDataRepository.class.getSimpleName();
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	private final CouchDbConnector connector;

	@Inject
	SourceDataRepository(@Assisted String databaseName, CouchDbInstance couchDbInstance) {
		super(JsonNode.class, couchDbInstance.createConnector(databaseName, true));
		this.connector = couchDbInstance.createConnector(databaseName, true);
		initStandardDesignDocument();
	}


	public List<JsonNode> executeQuery(OdsView view, String param) {
		return queryView(view.getViewName(), param);
	}


	public List<JsonNode> executeQuery(OdsView view) {
		return queryView(view.getViewName());
	}


	public void addView(OdsView odsView) {
		Assert.assertNotNull(odsView);

		DesignDocument designDocument;
		boolean update = false;

		if (connector.contains(DESIGN_DOCUMENT_ID)) {
			designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		} else {
			designDocument = designFactory.newDesignDocumentInstance();
			designDocument.setId(DESIGN_DOCUMENT_ID);
			update = true;
		}

		DesignDocument.View view = new DesignDocument.View(odsView.getMapFunction());
		designDocument.addView(odsView.getViewName(), view);

		try {
			if (update) connector.update(designDocument);
			else connector.create(designDocument);
		} catch (UpdateConflictException uce) {
			Log.error("failed to add view", uce);
		}
	}


	public boolean containsView(OdsView odsView) {
		Assert.assertNotNull(odsView);

		if (!connector.contains(DESIGN_DOCUMENT_ID)) return false;
		DesignDocument designDocument = connector.get(DesignDocument.class, DESIGN_DOCUMENT_ID);
		return designDocument.containsView(odsView.getViewName());
	}

}
