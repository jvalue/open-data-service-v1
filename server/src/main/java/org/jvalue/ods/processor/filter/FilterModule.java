/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import org.jvalue.ods.processor.filter.domain.PegelBrandenburgMerger;
import org.jvalue.ods.processor.filter.domain.PegelOnlineMerger;

public final class FilterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
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
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_PEGEL_ONLINE_MERGER),
						PegelOnlineMerger.class)
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_PEGEL_BRANDENBURG_MERGER),
						PegelBrandenburgMerger.class)
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_ADD_TIMESTAMP_FILTER),
						AddTimestampFilter.class)
				.implement(
						new TypeLiteral<Filter<ObjectNode, ObjectNode>>() {
						},
						Names.named(FilterFactory.NAME_TRANSFORMATION_FILTER),
						TransformationFilter.class)
				.build(FilterFactory.class));
	}

}
