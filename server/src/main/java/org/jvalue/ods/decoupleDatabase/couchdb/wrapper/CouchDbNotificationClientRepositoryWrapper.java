package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.db.NotificationClientRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.util.List;

public class CouchDbNotificationClientRepositoryWrapper implements IRepository<Client>{

	private NotificationClientRepository notificationClientRepository;

	@Inject
	CouchDbNotificationClientRepositoryWrapper(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		this.notificationClientRepository = new NotificationClientRepository(dbConnectorFactory, databaseName);
	}


	@Override
	public Client findById(String Id) {
		return notificationClientRepository.findById(Id);
	}


	@Override
	public void add(Client Value) {
		notificationClientRepository.add(Value);
	}


	@Override
	public void update(Client value) {
		notificationClientRepository.update(value);
	}


	@Override
	public void remove(Client Value) {
		notificationClientRepository.remove(Value);
	}


	@Override
	public List<Client> getAll() {
		return notificationClientRepository.getAll();
	}
}
