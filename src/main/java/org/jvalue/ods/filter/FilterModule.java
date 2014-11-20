package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.jvalue.ods.filter.adapter.AdapterModule;

public final class FilterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new AdapterModule());
		install(new FactoryModuleBuilder()
				.implement(
						new TypeLiteral<Filter<ArrayNode, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_NOTIFICATION_FILTER),
						NotificationFilter.class)
				.implement(
						new TypeLiteral<Filter<ArrayNode, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_DB_INSERTION_FILTER),
						DbInsertionFilter.class)
				.build(FilterFactory.class));
		bind(FilterChainManager.class).in(Singleton.class);
	}

}