package org.jvalue.ods.processor.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public final class FilterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
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
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_INT_TO_STRING_KEY_FILTER),
						IntToStringKeyFilter.class)
				.implement(
						new TypeLiteral<Filter<ArrayNode, ArrayNode>>() {
						},
						Names.named(FilterFactory.NAME_INVALID_DOCUMENT_FILTER),
						InvalidDocumentFilter.class)
				.build(FilterFactory.class));
	}

}
