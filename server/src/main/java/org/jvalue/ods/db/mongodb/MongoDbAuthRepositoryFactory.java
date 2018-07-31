package org.jvalue.ods.db.mongodb;

import com.google.inject.Inject;
import org.jvalue.commons.auth.BasicCredentials;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.auth.mongodb.MongoDbBasicCredentialsRepository;
import org.jvalue.commons.auth.mongodb.MongoDbUserRepository;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.factories.AuthRepositoryFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.db.repositories.GenericUserRepository;

public class MongoDbAuthRepositoryFactory implements AuthRepositoryFactory {

	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbAuthRepositoryFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericUserRepository<User> createUserRepository() {
		return new MongoDbUserRepository(dbConnectorFactory);
	}


	@Override
	public GenericRepository<BasicCredentials> createBasicCredentialRepository() {
		return new MongoDbBasicCredentialsRepository(dbConnectorFactory);
	}
}
