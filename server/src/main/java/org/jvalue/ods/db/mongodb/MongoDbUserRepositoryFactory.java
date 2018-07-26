package org.jvalue.commons.auth;

import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;

import javax.inject.Inject;

public class MongoDbUserRepositoryFactory implements UserRepositoryFactory {
	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbUserRepositoryFactory(DbConnectorFactory dbConnectorFactory){
		this.dbConnectorFactory = dbConnectorFactory;
	}

	@Override
	public GenericRepository<User> createUserRepository() {
		return null;
	}
}
