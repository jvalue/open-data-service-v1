package org.jvalue.ods.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.jvalue.ods.filter.adapter.AdapterModule;
import org.jvalue.ods.filter.reference.ReferenceModule;

public final class FilterModule extends AbstractModule {

	@Override
	protected void configure() {
		FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder()
				.implement(
						new TypeLiteral<Filter<ArrayNode, ArrayNode>>() {
						},
						Names.named(FilterFactory.NAME_NOTIFICATION_FILTER),
						NotificationFilter.class)
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_DB_INSERTION_FILTER),
						DbInsertionFilter.class)
				.implement(
						new TypeLiteral<Filter<ArrayNode, ArrayNode>>() {
						},
						Names.named(FilterFactory.NAME_INVALID_DOCUMENT_FILTER),
						InvalidDocumentFilter.class);

		install(new AdapterModule(factoryModuleBuilder));
		install(new ReferenceModule());
		install(factoryModuleBuilder.build(FilterFactory.class));
		bind(FilterChainManager.class).in(Singleton.class);
		bind(FilterChainFactory.class).in(Singleton.class);
	}

}
