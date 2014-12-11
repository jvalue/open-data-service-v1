package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.FilterFactory;

public final class AdapterModule extends AbstractModule {

	public AdapterModule(FactoryModuleBuilder factoryModuleBuilder) {
		factoryModuleBuilder
				.implement(
						new TypeLiteral<Filter<Void, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_JSON_SOURCE_ADAPTER),
						JsonSourceAdapter.class)
				.implement(
						new TypeLiteral<Filter<Void, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_CSV_SOURCE_ADAPTER),
						CsvSourceAdapter.class)
				.implement(
						new TypeLiteral<Filter<Void, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_XML_SOURCE_ADAPTER),
						XmlSourceAdapter.class)
				.implement(
						new TypeLiteral<Filter<Void, ArrayNode>>() { },
						Names.named(FilterFactory.NAME_OSM_SOURCE_ADAPTER),
						OsmSourceAdapter.class);
	}

	@Override
	protected void configure() {
		// nothing to do here
	}

}
