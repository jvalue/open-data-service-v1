package org.jvalue.ods.db.couchdb;

import com.google.inject.Inject;
import org.jvalue.commons.auth.BasicCredentials;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.auth.couchdb.BasicCredentialsRepository;
import org.jvalue.commons.auth.couchdb.UserRepository;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.factories.AuthRepositoryFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.db.repositories.GenericUserRepository;

public class CouchDbAuthRepositoryFactory implements AuthRepositoryFactory {

	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public CouchDbAuthRepositoryFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericUserRepository<User> createUserRepository() {
		return new UserRepository(dbConnectorFactory);
	}


	@Override
	public GenericRepository<BasicCredentials> createBasicCredentialRepository() {
		return new BasicCredentialsRepository(dbConnectorFactory);
	}
}
