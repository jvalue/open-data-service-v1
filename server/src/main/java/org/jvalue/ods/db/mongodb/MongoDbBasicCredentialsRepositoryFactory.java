package org.jvalue.ods.db.mongodb;

import org.jvalue.commons.auth.BasicCredentials;
import org.jvalue.commons.auth.BasicCredentialsRepositoryFactory;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;

import javax.inject.Inject;

public class MongoDbBasicCredentialsRepositoryFactory implements BasicCredentialsRepositoryFactory {
	private final DbConnectorFactory dbConnectorFactory;


	@Inject
	public MongoDbBasicCredentialsRepositoryFactory(DbConnectorFactory dbConnectorFactory) {
		this.dbConnectorFactory = dbConnectorFactory;
	}


	@Override
	public GenericRepository<BasicCredentials> createBasicCredentialRepository() {
		return null;
	}
}
