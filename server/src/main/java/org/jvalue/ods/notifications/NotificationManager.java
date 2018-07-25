package org.jvalue.ods.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.jvalue.commons.utils.Cache;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.QueryObject;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.db.couchdb.RepositoryFactory;
import org.jvalue.commons.db.GenericDataRepository;
import org.jvalue.commons.db.GenericRepository;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderCache;
import org.jvalue.ods.notifications.sender.SenderResult;


public final class NotificationManager
		extends AbstractDataSourcePropertyManager<Client, GenericRepository<Client>>
		implements DataSink {


	private final SenderCache senderCache;

	@Inject
	NotificationManager(
			Cache<GenericRepository<Client>> repositoryCache,
			RepositoryFactory repositoryFactory,
			SenderCache senderCache) {

		super(repositoryCache, repositoryFactory);
		this.senderCache = senderCache;
	}


	@Override
	public void onNewDataStart(DataSource source) {
		for (Client client : getAll(source)) {
			senderCache.get(source, client).onNewDataStart();
		}
	}


	@Override
	public void onNewDataItem(DataSource source, ObjectNode data) {
		for (Client client : getAll(source)) {
			senderCache.get(source, client).onNewDataItem(data);
		}
	}


	@Override
	public void onNewDataComplete(DataSource source) {
		for (Client client : getAll(source)) {
			Sender<?> sender = senderCache.get(source, client);
			senderCache.release(source, client);

			sender.onNewDataComplete();
			SenderResult result = sender.getSenderResult();
			switch(result.getStatus()) {
				case SUCCESS:
					break;

				case ERROR:
					String errorMsg = "Failed to send notification to client " + client.getId();
					if (result.getErrorCause() != null)
						errorMsg = errorMsg + " (" + result.getErrorCause().getMessage() + ")";
					if (result.getErrorMsg() != null)
						errorMsg = errorMsg + " (" + result.getErrorMsg() + ")";
					Log.error(errorMsg);
					break;

				case REMOVE_CLIENT:
					Log.info("Unregistering client " + result.getOldClient().getId());
					remove(source, null, result.getOldClient());
					break;

				case UPDATE_CLIENT:
					Log.info("Updating client id to " + result.getNewClient().getId());
					remove(source, null, result.getOldClient());
					add(source, null, result.getNewClient());
					break;
			}
		}
	}


	@Override
	protected void doAdd(DataSource source, GenericDataRepository<CouchDbDataView, JsonNode> dataRepository, Client client) { }


	@Override
	protected void doRemove(DataSource source, GenericDataRepository<CouchDbDataView, JsonNode> dataRepository, Client client) { }


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected GenericRepository<Client> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createNotificationClientRepository(sourceId);
	}

}
