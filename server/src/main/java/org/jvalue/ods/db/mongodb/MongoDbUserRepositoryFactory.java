package org.jvalue.ods.db.mongodb;

import org.jvalue.commons.auth.GenericUserRepository;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.auth.UserRepositoryFactory;
import org.jvalue.commons.db.DbConnectorFactory;

import javax.inject.Inject;

public class MongoDbUserRepositoryFactory implements UserRepositoryFactory {
	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbUserRepositoryFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericUserRepository<User> createUserRepository() {
		//TODO: implement MongoDbUserRepository
		return null;
	}
}
