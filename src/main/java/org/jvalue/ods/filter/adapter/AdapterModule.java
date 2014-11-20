package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import org.jvalue.ods.filter.Filter;
import org.w3c.dom.Document;

import java.io.File;

public final class AdapterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(new TypeLiteral<Filter<Void, Document>>() {
				}, XmlSourceAdapter.class)
				.implement(new TypeLiteral<Filter<Void, ArrayNode>>() { }, JsonSourceAdapter.class)
				.implement(new TypeLiteral<Filter<Void, File>>() { }, FileSourceAdapter.class)
				.build(SourceAdapterFactory.class));
	}

}
