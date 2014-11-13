package org.jvalue.ods.filter.grabber;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import org.jvalue.ods.filter.Filter;
import org.w3c.dom.Document;

import java.io.File;

public final class GrabberModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
				.implement(new TypeLiteral<Filter<Void, Document>>() { }, XmlGrabber.class)
				.implement(new TypeLiteral<Filter<Void, JsonNode>>() { }, JsonGrabber.class)
				.implement(new TypeLiteral<Filter<Void, File>>() { }, FileGrabber.class)
				.implement(new TypeLiteral<Filter<Void, String>>() { }, HttpGrabber.class)
				.build(GrabberFactory.class));
	}

}
